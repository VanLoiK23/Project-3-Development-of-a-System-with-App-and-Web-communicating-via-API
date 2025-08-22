package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.repository.CartRepository
import com.example.quanlybandienthoai.model.repository.CartRepository.checkSL
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CartViewModel(val context: Application) : AndroidViewModel(context) {
    private val cartRepository = CartRepository

    var currentUser: User? = null

    private val _userCart = MutableStateFlow<Map<Sanpham, List<Phienbansanpham>>>(emptyMap())
    val userCart: StateFlow<Map<Sanpham, List<Phienbansanpham>>> = _userCart.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _totalTransport = MutableStateFlow(200000.000)
    val totalTransport: StateFlow<Double> = _totalTransport.asStateFlow()

    private val _totalTax = MutableStateFlow(10)
    val totalTax: StateFlow<Int> = _totalTax.asStateFlow()

    private val _grandPrice = MutableStateFlow(0.0)
    val grandPrice: StateFlow<Double> = _grandPrice.asStateFlow()

    private val _cartItem = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItem: StateFlow<List<CartItem>> = _cartItem.asStateFlow()

    private val _cartID = MutableStateFlow<Int>(0)
    val cartId: StateFlow<Int> = _cartID.asStateFlow()

    var isLoading by mutableStateOf(false)

    var message by mutableStateOf("")

    var messWarning by mutableStateOf("")

    var isWarning = mutableStateOf(false)

    var idCart by mutableStateOf(-1)

    private var idCartListener: ListenerRegistration? = null
    private var cartItemsListener: ListenerRegistration? = null
    private var cartDetailsListener: ListenerRegistration? = null


    fun clear() {
        _userCart.value = emptyMap()
        _cartItem.value = emptyList()
        isLoading = false
        idCart = -1
        super.onCleared()
        idCartListener?.remove()
        cartItemsListener?.remove()
        cartDetailsListener?.remove()
    }

    override fun onCleared() {
        super.onCleared()
        idCartListener?.remove()
        cartItemsListener?.remove()
        cartDetailsListener?.remove()
    }

    fun setUser(user: User?) {
        isLoading = true
        this.currentUser = user
        getUserCart()
        isLoading = false
    }

    fun getIdCart(idClient: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = CartRepository.getIdCart(idClient)
            if (id != null) {
                idCart = id
            }
        }
    }

    fun checkOut(onCheck: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = currentUser?.id?.toInt() ?: return@launch

            val cartItems = cartRepository.getListCartItem(userId)

            Log.d("CartItem: ", cartItems.toString())

            for (cartItem in cartItems) {

                Log.d("RunIn", "runnn")

                val mess = cartRepository.checkOutISValid(cartItem)

                if (mess != null) {
                    isWarning.value = true
                    messWarning = mess

                    withContext(Dispatchers.Main) {
                        onCheck(mess) // báo lỗi quá tồn kho
                    }
                    Log.d("messageTest", mess.toString())


                    break // dừng kiểm tra các sản phẩm còn lại
                }

                Log.d("messageTest", mess.toString())
            }

            // Nếu duyệt hết vòng lặp mà không có lỗi
            if (messWarning.isEmpty()) {
                withContext(Dispatchers.Main) {
                    onCheck(null) // không có lỗi → có thể tiếp tục
                }
            }
        }
    }

    private fun getUserCart() {
        viewModelScope.launch(Dispatchers.IO) {

            if (idCart == -1) {

                val userId = currentUser?.id?.toInt() ?: return@launch

                idCartListener?.remove()
                idCartListener = cartRepository.listenIdCart(userId) { cartId ->
                    if (cartId != null) {
                        // Lắng nghe cart items
                        cartItemsListener?.remove()
                        cartItemsListener = cartRepository.listenCartItems(cartId) { items ->
                            _cartItem.value = items
                        }

                        // Lắng nghe cart details
                        cartDetailsListener?.remove()
                        cartDetailsListener =
                            cartRepository.listenCartDetailsGroupedByProduct(cartId) { details ->
                                _userCart.value = details
                                calculatePrice()
                            }
                        idCart = cartId
                    } else {
                        cartItemsListener?.remove()
                        cartDetailsListener?.remove()

                        _cartItem.value = emptyList()
                        _userCart.value = emptyMap()
                    }
                }

                // Chạy song song 2 tác vụ để tối ưu thời gian tải

//                val cartDeferred = async { cartRepository.getCartDetailsGroupedByProduct(userId) }
//                val cartItemsDeferred = async { cartRepository.getListCartItem(userId) }
//
//                val cartData = cartDeferred.await()   // Chờ kết quả giỏ hàng
//                val cartItemsData = cartItemsDeferred.await() // Chờ kết quả cart item

                // Cập nhật state sau khi có dữ liệu
//                _userCart.value = cartData
//                _cartItem.value = cartItemsData
//
//                if (cartItemsData.isNotEmpty()) {
//                    idCart = cartItemsData[0].cart_id
//                }
                // Tính tổng tiền
//                val listSL = cartItemsData.map { it.soluong.toInt() }
//                var total = 0.0
//                var i = 0
//                cartData.forEach { (_, phienbansanphams) ->
//                    phienbansanphams.forEach { pb ->
//                        total += pb.price_sale * (listSL.getOrNull(i) ?: 1)
//                        i++
//                    }
//                }
//
//                _totalPrice.value = total
//
//                // Reset giá trị grandPrice
//                _grandPrice.value = 0.0
//
//                if (total > 2_000_000) {
//                    _totalTransport.value = 0.0
//                    _grandPrice.value = total + (total / _totalTax.value)
//                } else {
//                    _totalTransport.value = 200_000.0
//                    _grandPrice.value = (total + _totalTransport.value) +
//                            ((total + _totalTransport.value) / _totalTax.value)
//                }
            }
        }
    }

    private fun calculatePrice() {
        val cartItemsData = _cartItem.value
        val cartData = _userCart.value

        val listSL = cartItemsData.map { it.soluong.toInt() }
        var total = 0.0
        var i = 0
        cartData.forEach { (_, phienbansanphams) ->
            phienbansanphams.forEach { pb ->
                total += pb.price_sale * (listSL.getOrNull(i) ?: 1)
                i++
            }
        }

        _totalPrice.value = total
        _grandPrice.value = if (total > 5_000_000) {
            _totalTransport.value = 0.0
            total + (total / _totalTax.value)
        } else {
            _totalTransport.value = 200_000.0
            (total + _totalTransport.value) + ((total) / _totalTax.value)
        }
    }

//    private fun getUserCart() {
//        viewModelScope.launch(Dispatchers.IO) {
//
//            if (idCart == -1) {
//
//                val userId = currentUser?.id?.toInt() ?: return@launch
//
//                // Chạy song song 2 tác vụ để tối ưu thời gian tải
//
//                val cartDeferred = async { cartRepository.getCartDetailsGroupedByProduct(userId) }
//                val cartItemsDeferred = async { cartRepository.getListCartItem(userId) }
//
//                val cartData = cartDeferred.await()   // Chờ kết quả giỏ hàng
//                val cartItemsData = cartItemsDeferred.await() // Chờ kết quả cart item
//
//                // Cập nhật state sau khi có dữ liệu
//                _userCart.value = cartData
//                _cartItem.value = cartItemsData
//
//                if (cartItemsData.isNotEmpty()) {
//                    idCart = cartItemsData[0].cart_id
//                }
//                // Tính tổng tiền
//                val listSL = cartItemsData.map { it.soluong.toInt() }
//                var total = 0.0
//                var i = 0
//                cartData.forEach { (_, phienbansanphams) ->
//                    phienbansanphams.forEach { pb ->
//                        total += pb.price_sale * (listSL.getOrNull(i) ?: 1)
//                        i++
//                    }
//                }
//
//                _totalPrice.value = total
//
//                // Reset giá trị grandPrice
//                _grandPrice.value = 0.0
//
//                if (total > 2_000_000) {
//                    _totalTransport.value = 0.0
//                    _grandPrice.value = total + (total / _totalTax.value)
//                } else {
//                    _totalTransport.value = 200_000.0
//                    _grandPrice.value = (total + _totalTransport.value) +
//                            ((total + _totalTransport.value) / _totalTax.value)
//                }
//            }
//        }
//    }


    fun updateQty(cartItem: CartItem, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser?.id?.let {
                cartRepository.updateQuantity(
                    it.toInt(),
                    cartItem.maphienbansp,
                    quantity
                ) { check ->
                    if (check) {
//                        getUserCart()
                        val currentCartItems = _cartItem.value.toMutableList()

                        val updatedCartItems = currentCartItems.map {
                            if (it.maphienbansp == cartItem.maphienbansp) {
                                it.copy(soluong = it.soluong + quantity)
                            } else {
                                it
                            }
                        }

                        _cartItem.value = updatedCartItems

                        updateTongTien()
                    } else {
                        message = "Số lượng vượt quá tồn kho"
                    }
                }

            }
        }
    }

//    fun deleteCartItem(cart: Int, cartId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            cartRepository.deleteCartItem(cart, cartId)
//
//            getUserCart()
//        }
//    }

//    fun deleteCartItem(cart: Int, cartId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//
//
//            val currentUserCart = _userCart.value.
//            val updatedCartItems = currentCartItems.filterNot { it.cart_id == cartId }
//
//            _cartItem.value = updatedCartItems // cập nhật lại để UI biết thay đổi
//
//
//            val currentCartItems = _cartItem.value.toMutableList()
//            val updatedCartItems = currentCartItems.filterNot { it.cart_id == cartId }
//
//            _cartItem.value = updatedCartItems // cập nhật lại để UI biết thay đổi
//
//            updateTongTien()
//
//            cartRepository.deleteCartItem(cart, cartId)
//
//        }
//    }

    fun deleteCartItem(cartID: Int, cartItemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Xoá item trong list cart hiện tại
            val currentCartItems = _cartItem.value.toMutableList()
            val deletedItem = currentCartItems.find { it.cart_item_id == cartItemId }

            if (deletedItem != null) {
                currentCartItems.remove(deletedItem)
                _cartItem.value = currentCartItems

                // 2. Xoá luôn variant trong _userCart nếu không còn item nào của sản phẩm đó
                val currentUserCart = _userCart.value.toMutableMap()
                val product = currentUserCart.keys.find { it.masp == deletedItem.masp }

                product?.let {
                    val variants = currentUserCart[it]?.toMutableList()
                    variants?.removeIf { variant -> variant.maphienbansp == deletedItem.maphienbansp }

                    if (variants.isNullOrEmpty()) {
                        currentUserCart.remove(it)
                    } else {
                        currentUserCart[it] = variants
                    }
                    _userCart.value = currentUserCart
                }

                // 3. Gọi repo để xoá khỏi database
                cartRepository.deleteCartItem(cartID, cartItemId)

                // 4. Cập nhật lại tổng tiền
                updateTongTien()
            }
        }
    }


//    fun insertCart(cart: Cart, cartItem: CartItem) {
//        viewModelScope.launch(Dispatchers.IO) {
////            currentUser?.id?.let {
//            if (checkSL(cartItem, cartItem.soluong)) {
//                val idCart = cartRepository.insertCart(cart)
//
//                if (idCart != 0) {
//                    cartRepository.insertCartItem(idCart, cartItem)
//
//                    message = "Thêm sản phẩm vào giỏ hàng thành công"
//                } else {
//                    message = "Không thể tạo giỏ hàng"
//                }
//            } else {
//                message = "Số lượng vượt quá tồn kho"
//            }
////                }
//
//            getUserCart()
//        }
//    }

    fun insertCart(cart: Cart, cartItem: CartItem, product: Sanpham, variant: Phienbansanpham) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkSL(cartItem, cartItem.soluong)) {
                val idCart = cartRepository.insertCart(cart)

                if (idCart != 0) {
                    val (cartItemID, isExist) = cartRepository.insertCartItem(idCart, cartItem)

                    // ✅ Cập nhật _cartItem
                    if (cartItemID != 0) {
                        _cartItem.value = _cartItem.value.toMutableList().apply {
                            add(
                                cartItem.copy(
                                    cart_id = idCart,
                                    cart_item_id = cartItemID,
                                    soluong = cartItem.soluong
                                )
                            ) // gán idCart cho cartItem
                        }

                        // ✅ Cập nhật _userCart
                        val currentUserCart = _userCart.value.toMutableMap()
                        val existingVariants =
                            currentUserCart[product]?.toMutableList() ?: mutableListOf()

                        val variantExists =
                            existingVariants.any { it.maphienbansp == variant.maphienbansp }
                        if (!variantExists) {
                            existingVariants.add(variant)
                            currentUserCart[product] = existingVariants
                            _userCart.value = currentUserCart
                        }

                        updateTongTien()
                        message = "Thêm sản phẩm vào giỏ hàng thành công"
                    }
                } else {
                    message = "Không thể tạo giỏ hàng"
                }
            } else {
                message = "Số lượng vượt quá tồn kho"
            }
        }
    }


//    fun insertCartItem(cartId: Int, cartItem: CartItem) {
//        viewModelScope.launch(Dispatchers.IO) {
////            currentUser?.id?.let {
//
//            if (checkSL(cartItem, cartItem.soluong)) {
//
//                _cartItem.value = _cartItem.value.toMutableList().apply {
//                    add(cartItem)
//                }
//
//                updateTongTien()
//
//                cartRepository.insertCartItem(cartId, cartItem)
//
//                message = "Thêm sản phẩm vào giỏ hàng thành công"
//            } else {
//                message = "Số lượng vượt quá tồn kho"
//            }
////                }
//
// //           getUserCart()
//        }
//    }

    fun insertCartItem(
        cartId: Int,
        cartItem: CartItem,
        product: Sanpham,
        variant: Phienbansanpham
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkSL(cartItem, cartItem.soluong)) {
                // Thêm vào _cartItem
                val (cartItemID, isExist) = cartRepository.insertCartItem(cartId, cartItem)

                // Chưa có cartItem
                if (cartItemID != 0 && !isExist) {
                    _cartItem.value = _cartItem.value.toMutableList().apply {
                        add(
                            cartItem.copy(
                                cart_id = cartId,
                                cart_item_id = cartItemID,
                                soluong = cartItem.soluong
                            )
                        )
                    }
                }
                // Đã có cartItem
                else if (isExist) {
                    _cartItem.value = _cartItem.value.map {
                        if (it.maphienbansp == cartItem.maphienbansp) {
                            it.copy(
                                cart_id = cartId,
                                cart_item_id = cartItemID,
                                soluong = it.soluong + cartItem.soluong
                            )
                        } else {
                            it
                        }
                    }
                }


                // Cập nhật _userCart
                val currentUserCart = _userCart.value.toMutableMap()
                val variantsList = currentUserCart[product]?.toMutableList() ?: mutableListOf()

                // Kiểm tra nếu variant đã tồn tại thì không thêm nữa
                val variantExists = variantsList.any { it.maphienbansp == variant.maphienbansp }
                if (!variantExists) {
                    variantsList.add(variant)
                    currentUserCart[product] = variantsList
                    _userCart.value = currentUserCart
                }

                updateTongTien()

                // Gọi repository thêm DB

                message = "Thêm sản phẩm vào giỏ hàng thành công"
            } else {
                message = "Số lượng vượt quá tồn kho"
            }
        }
    }


    var isExist = mutableStateOf(false)


    fun checkCartIdKH(idKH: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val idCart = cartRepository.getIdCart(idKH)
            if (idCart != null) {
                _cartID.value = idCart.toInt()
                isExist.value = true
                onResult(true)
            } else {
                isExist.value = false
                onResult(false)
            }
        }
    }


    fun deleteAllCartItem(cart: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _userCart.value = emptyMap()
            _cartItem.value = emptyList()
            cartRepository.deleteAllCartItems(cart)
        }
    }


    fun updateComplete(cartId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.updateCartComplete(cartId)
        }
    }


    //    fun addToCart(product: Sanpham, variant: Phienbansanpham, soluong: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val cartIt = CartItem(-1, -1, variant.maphienbansp, product.masp, soluong)
//
//            if (checkSL(cartIt, soluong)) {
//                val currentMap = _userCart.value.toMutableMap()
//                val existingVariants = currentMap[product]?.toMutableList() ?: mutableListOf()
//                val variantExists = existingVariants.any { it.maphienbansp == variant.maphienbansp }
//
//                // Nếu chưa có variant này thì thêm vào danh sách variant
//                if (!variantExists) {
//                    existingVariants.add(variant)
//                    currentMap[product] = existingVariants
//                    _userCart.value = currentMap
//                }
//
//                // Cập nhật hoặc thêm mới trong cartItem
//                val currentCartItems = _cartItem.value.toMutableList()
//                val existingCartItem =
//                    currentCartItems.find { it.maphienbansp == variant.maphienbansp }
//
//                if (existingCartItem != null) {
////                    existingCartItem.soluong += soluong
//
//                    if (checkSL(cartIt, soluong)) {
//                        val updatedCartItems = currentCartItems.map {
//                            if (it.maphienbansp == variant.maphienbansp) {
//                                it.copy(soluong = it.soluong + soluong)
//                            } else {
//                                it
//                            }
//                        }
//                        _cartItem.value = updatedCartItems
//                    } else {
//                        message = "Số lượng vượt quá tồn kho"
//                    }
//
//                } else {
//                    currentCartItems.add(cartIt)
//
//                    _cartItem.value = currentCartItems
//                }
//
//
//                updateTongTien()
//                message = "Thêm sản phẩm vào giỏ hàng thành công"
//            } else {
//                message = "Số lượng vượt quá tồn kho"
//            }
//        }
//    }
//
//
////    fun deletedCartItem(variantToDelete: Phienbansanpham) {
////        val currentCart = _userCart.value.toMutableMap()
////
////        // Tìm sản phẩm chứa variant này
////        val productToUpdate = currentCart.entries.find { (_, variants) ->
////            variants.any { it.maphienbansp == variantToDelete.maphienbansp }
////        } ?: return
////
////        val (product, variants) = productToUpdate
////
////        // Lọc bỏ variant cần xoá
////        val updatedVariants = variants.filterNot { it.maphienbansp == variantToDelete.maphienbansp }
////
////        if (updatedVariants.isEmpty()) {
////            // Nếu không còn variant nào => xóa luôn sản phẩm khỏi giỏ
////            currentCart.remove(product)
////        } else {
////            // Cập nhật lại danh sách variant của sản phẩm
////            currentCart[product] = updatedVariants
////        }
////
////        _userCart.value = currentCart
////    }
//
//    fun deletedCartItem(maphienbansp: Int) {
//        val updatedCart = _userCart.value.mapValues { (_, variants) ->
//            variants.filterNot { it.maphienbansp == maphienbansp }
//        }.filterValues { it.isNotEmpty() }
//
//        _userCart.value = updatedCart
//
//        // Cập nhật _cartItem
//        val updatedCartItems = _cartItem.value.filterNot { it.maphienbansp == maphienbansp }
//        _cartItem.value = updatedCartItems
//
//        updateTongTien()
//    }
//
//
//    fun deletedAllCartItem() {
//        _userCart.value = emptyMap()
//
//        resetTongTien()
//    }
//
////    fun updateSoluong(cartItem: CartItem, quantity: Int) {
////        viewModelScope.launch(Dispatchers.IO) {
////            val cartIt = CartItem(-1, -1, cartItem.maphienbansp, cartItem.masp, quantity)
////
////            if (checkSL(cartIt, quantity)) {
//////                val updatedCart = _userCart.value.mapValues { (sanpham, variants) ->
//////                    variants.map { variant ->
//////                        if (variant.maphienbansp == cartItem.maphienbansp) {
//////                            val soluongton = variant.soluongton
//////
//////                            variant.copy(soluongton = soluongton + quantity)
//////                        } else variant
//////                    }
//////                }
//////                _userCart.value = updatedCart
////
////                val currentCartItems = _cartItem.value.toMutableList()
////                val existingCartItem = currentCartItems.find { it.maphienbansp == cartItem.maphienbansp }
////
////                if (existingCartItem != null) {
////                    existingCartItem.soluong += quantity
////                } else {
////                    currentCartItems.add(cartItem)
////                }
////
////                _cartItem.value = currentCartItems
////
////                updateTongTien()
////            } else {
////                message = "Số lượng vượt quá tồn kho"
////            }
////        }
////    }
//
    fun updateTongTien() {
        val items = _cartItem.value
        var total = 0.0

        for (item in items) {
            val variant = findVariant(item.maphienbansp)
            if (variant != null) {
                total += variant.price_sale * item.soluong
            }
        }

        _totalPrice.value = total
        _grandPrice.value = 0.0

        if (total > 2_000_000) {
            _totalTransport.value = 0.0
            _grandPrice.value = total + (total / _totalTax.value)
        } else {
            _totalTransport.value = 200_000.0
            _grandPrice.value = (total + _totalTransport.value) +
                    ((total + _totalTransport.value) / _totalTax.value)
        }
    }

    private fun findVariant(maphienban: Int): Phienbansanpham? {
        return _userCart.value.values.flatten().find { it.maphienbansp == maphienban }
    }
//
//
//    fun resetTongTien() {
//        _totalPrice.value = 0.0
//
//        _grandPrice.value = 0.0
//
//        _totalTransport.value = 0.0
//    }


}
