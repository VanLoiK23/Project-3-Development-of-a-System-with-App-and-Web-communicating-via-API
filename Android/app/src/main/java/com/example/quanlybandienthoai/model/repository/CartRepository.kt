package com.example.quanlybandienthoai.model.repository


import android.util.Log
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.repository.ProductRepository.insertPhienBanSanPham
import com.example.quanlybandienthoai.model.repository.ProductRepository.insertProduct
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object CartRepository {
    val db by lazy { Firebase.firestore }
    private val cartCollectionRef by lazy { db.collection("cart") }
    private val productCollectionRef by lazy { db.collection("sanpham") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }


    suspend fun getIdCart(userId: Int): Int? {
        val snapshot = cartCollectionRef
            .whereEqualTo("idkh", userId)
            .whereNotEqualTo("status", "completed")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .limit(1)
            .get().await()

        return snapshot.documents.firstOrNull()?.getLong("id")?.toInt()
    }

    fun listenIdCart(userId: Int, onResult: (Int?) -> Unit): ListenerRegistration {
        return cartCollectionRef
            .whereEqualTo("idkh", userId)
            .whereNotEqualTo("status", "completed")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onResult(null)
                    return@addSnapshotListener
                }
                val id = snapshot?.documents?.firstOrNull()?.getLong("id")?.toInt()
                onResult(id)
            }
    }



    suspend fun getCartDetailsGroupedByProduct(userId: Int): Map<Sanpham, List<Phienbansanpham>> {
        val cartId = getIdCart(userId) ?: return emptyMap()
        if (cartId <= 0) return emptyMap()

        val cartItemsSnapshot = cartCollectionRef.document(cartId.toString())
            .collection("cart_items").get().await()

        val cartItems = cartItemsSnapshot.toObjects(CartItem::class.java)
        if (cartItems.isEmpty()) return emptyMap()

        val sanphamMap = mutableMapOf<Sanpham, MutableList<Phienbansanpham>>()

        // Group cart items theo masp
        val grouped = cartItems.groupBy { it.masp }

        for ((masp, items) in grouped) {
            val sanphamDoc = productCollectionRef.document(masp.toString()).get().await()
            val sanpham = sanphamDoc.toObject(Sanpham::class.java) ?: continue

            val versionIds = items.map { it.maphienbansp.toString() }
            val versionDocs = sanphamDoc.reference
                .collection("phienbansanpham")
                .whereIn(FieldPath.documentId(), versionIds)
                .get()
                .await()

            val versions = versionDocs.toObjects(Phienbansanpham::class.java)
            sanphamMap[sanpham] = versions.toMutableList()
        }

        return sanphamMap
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun listenCartDetailsGroupedByProduct(
        cartId: Int,
        onResult: (Map<Sanpham, List<Phienbansanpham>>) -> Unit
    ): ListenerRegistration {
        return cartCollectionRef.document(cartId.toString())
            .collection("cart_items")
            .addSnapshotListener { cartItemsSnapshot, e ->
                if (cartId <= 0) {
                    onResult(emptyMap())
                    return@addSnapshotListener
                }

                if (e != null || cartItemsSnapshot == null) {
                    onResult(emptyMap())
                    return@addSnapshotListener
                }

                val cartItems = cartItemsSnapshot.toObjects(CartItem::class.java)
                if (cartItems.isEmpty()) {
                    onResult(emptyMap())
                    return@addSnapshotListener
                }

                val grouped = cartItems.groupBy { it.masp }
                val sanphamMap = mutableMapOf<Sanpham, MutableList<Phienbansanpham>>()

                // Chạy song song để lấy dữ liệu chi tiết từng sản phẩm
                GlobalScope.launch {
                    for ((masp, items) in grouped) {
                        val sanphamDoc = productCollectionRef.document(masp.toString()).get().await()
                        val sanpham = sanphamDoc.toObject(Sanpham::class.java) ?: continue

                        val versionIds = items.map { it.maphienbansp.toString() }
                        val versionDocs = sanphamDoc.reference
                            .collection("phienbansanpham")
                            .whereIn(FieldPath.documentId(), versionIds)
                            .get()
                            .await()

                        val versions = versionDocs.toObjects(Phienbansanpham::class.java)
                        sanphamMap[sanpham] = versions.toMutableList()
                    }
                    onResult(sanphamMap)
                }
            }
    }



    suspend fun insertCart(cart: Cart): Int {
        return try {
            val documentId = getNextCartId()
            val newCart = cart.copy(id = documentId)
            cartCollectionRef.document(documentId.toString()).set(newCart).await() // Chờ hoàn tất
            documentId
        } catch (e: Exception) {
            Log.e("Firestore", "Error inserting cart", e)
            0
        }
    }

    suspend fun insertCartSynchFromMySql(cart: Cart) {
        try {
            cartCollectionRef.document(cart.id.toString()).set(cart).await() // Chờ hoàn tất
        } catch (e: Exception) {
            Log.e("Firestore", "Error inserting cart", e)
        }
    }

    suspend fun updateCartComplete(cartId: Int) {
        try {
            cartCollectionRef.document(cartId.toString())
                .update("status", "completed")
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun checkSL(cartItem: CartItem, soLuongCanThem: Int): Boolean {
        return try {
            val currentSL = getTotalSLByMaPhienBanSP(cartItem.maphienbansp)
            val soLuongTonKho = ProductRepository.getSLByMaPhienBanSP(cartItem)

            Log.d("currentSL", currentSL.toString())
            Log.d("soLuongTonKho", soLuongTonKho.toString())

            (currentSL + soLuongCanThem) <= soLuongTonKho
        } catch (e: Exception) {
            Log.e("checkSL error", e.message.toString())
            false
        }
    }

    suspend fun checkOutISValid(cartItem: CartItem): String? {
        try {
            val soLuongTonKho = ProductRepository.getSLByMaPhienBanSP(cartItem)

            if (cartItem.soluong > soLuongTonKho) {
                val tenSP = ProductRepository.getProductById(cartItem.masp.toString())?.tensp

                return "Phiên bản sản phẩm $tenSP không đủ số lượng tồn kho(tồn $soLuongTonKho )"
            } else {
                return null
            }
        } catch (e: Exception) {
            Log.e("checkSL error", e.message.toString())
            return e.toString()
        }
    }

    suspend fun getTotalSLByMaPhienBanSP(maphienbansp: Int): Int {
        var total = 0

        // Lấy tất cả các Cart có status != "completed"
        val cartsSnapshot = cartCollectionRef
            .whereEqualTo("status", "active")
            .get()
            .await()

        for (cartDoc in cartsSnapshot.documents) {
            val cartItemsSnapshot = cartDoc.reference
                .collection("cart_items")
                .whereEqualTo("maphienbansp", maphienbansp)
                .get()
                .await()

            for (item in cartItemsSnapshot.documents) {
                val soluong = item.getLong("soluong") ?: 0
                total += soluong.toInt()
            }
        }

        return total
    }


    suspend fun getNextCartId(): Int {
        val snapshot =
            cartCollectionRef.orderBy("id", Query.Direction.DESCENDING).limit(1).get().await()

        return if (!snapshot.isEmpty) {
            val lastId = snapshot.documents[0].getLong("id") ?: 0
            (lastId + 1).toInt()
        } else {
            1 // Nếu chưa có sản phẩm nào, bắt đầu từ 1
        }
    }

    suspend fun insertCartItem(cartId: Int, cartItem: CartItem): Pair<Int, Boolean> {
        var cartItemId = "0"
        try {
            // Truy vấn để kiểm tra xem maphienbansp đã có trong giỏ hàng chưa
            val existingCartItemSnapshot = cartCollectionRef.document(cartId.toString())
                .collection("cart_items")
                .whereEqualTo("maphienbansp", cartItem.maphienbansp)
                .get().await()

            if (existingCartItemSnapshot.isEmpty) {
                // Nếu chưa có, thêm mới sản phẩm vào giỏ hàng
                cartItemId = getNextCartItemId(cartId).toString()
                val newCartItem = cartItem.copy(cart_id = cartId, cart_item_id = cartItemId.toInt())

                cartCollectionRef.document(cartId.toString()) // Lấy Document theo masp
                    .collection("cart_items") // Collection con
                    .document(cartItemId) // ID của Document
                    .set(newCartItem).await()

                Log.d("Firestore", "Added new cart item successfully!")

                return (cartItemId.toInt() to false)
            } else {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng
                val existingCartItem = existingCartItemSnapshot.documents.firstOrNull()
                existingCartItem?.let {
                    cartItemId = it.getLong("cart_item_id").toString()

                    val updatedQuantity =
                        it.getLong("soluong")?.toInt()?.plus(cartItem.soluong) ?: 1

                    cartCollectionRef.document(cartId.toString())
                        .collection("cart_items")
                        .document(it.id) // Dùng ID của sản phẩm đã có trong giỏ
                        .update("soluong", updatedQuantity).await()

                    Log.d("Firestore", "Cart item quantity updated successfully!")
                }
                return (cartItemId.toInt() to true)
            }
        } catch (e: Exception) {
            Log.w("Firestore", "Error adding or updating document", e)

            return (0 to false)
        }

    }

    suspend fun getAllCarts(idkh: Int): Cart? {
        return try {

            val cartSnapshots = cartCollectionRef
                .whereEqualTo("idkh", idkh)
                .whereNotEqualTo("status", "completed")
                .get()
                .await()

            cartSnapshots.documents.map { cartDocument ->
                val cart =
                    cartDocument.toObject(Cart::class.java)?.copy(id = cartDocument.id.toInt())

                cart?.let {
                    val cartItemsSnapshot = cartCollectionRef.document(cart.id.toString())
                        .collection("cart_items").get().await()

                    val cartItems = cartItemsSnapshot.documents.mapNotNull { cartItemDocument ->
                        cartItemDocument.toObject(CartItem::class.java)?.copy(
                            cart_item_id = cartItemDocument.id.toInt(),
                            cart_id = cart.id
                        )
                    }

                    cart.copy(cartItems = cartItems)
                } ?: cart
            }.filterNotNull().firstOrNull()
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching carts", e)
            null
        }
    }


    suspend fun getNextCartItemId(cartId: Int): Int {
        val snapshot = cartCollectionRef.document(cartId.toString()).collection("cart_items")
            .orderBy("cart_item_id", Query.Direction.DESCENDING).limit(1).get().await()

        return if (!snapshot.isEmpty) {
            val lastId = snapshot.documents[0].getLong("cart_item_id") ?: 0
            (lastId + 1).toInt()
        } else {
            1
        }
    }

    suspend fun updateQuantity(
        userId: Int,
        maphienbansp: Int,
        quantity: Int,
        onResult: (Boolean) -> Unit
    ) {
        val caritemCollection =
            cartCollectionRef.document(getIdCart(userId).toString()).collection("cart_items")
        val queryResult =
            caritemCollection
                .whereEqualTo("maphienbansp", maphienbansp).get()
                .await().toObjects(
                    CartItem::class.java
                )
        val cart = queryResult[0]

        if (checkSL(cart, quantity)) {
            onResult(true)
            cart.soluong += quantity
            caritemCollection.document(cart.cart_item_id.toString()).set(cart)
        } else {
            onResult(false)
        }

    }

    suspend fun getQuantity(userId: Int, cartItem: Int): Int {
        val cartItemDoc = cartCollectionRef.document(getIdCart(userId).toString())
            .collection("cart_items").document(cartItem.toString()).get().await()

        return if (cartItemDoc.exists()) {
            cartItemDoc.getLong("soluong")?.toInt() ?: 0
        } else {
            1
        }
    }

    suspend fun getListCartItem(userId: Int): List<CartItem> {
        val cartItemDocs = cartCollectionRef.document(getIdCart(userId).toString())
            .collection("cart_items").get().await()

        return if (!cartItemDocs.isEmpty) {
            cartItemDocs.documents.mapNotNull { document ->
                document.toObject(CartItem::class.java)
            }
        } else {
            emptyList()
        }
    }

    fun listenCartItems(cartId: Int, onResult: (List<CartItem>) -> Unit): ListenerRegistration {
        return cartCollectionRef.document(cartId.toString())
            .collection("cart_items")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onResult(emptyList())
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(CartItem::class.java) ?: emptyList()
                onResult(items)
            }
    }




    suspend fun deleteAllCartItems(cartId: Int) {
        val cartItemsRef = cartCollectionRef
            .document(cartId.toString())
            .collection("cart_items")

        val snapshot = cartItemsRef.get().await()

        val batch = Firebase.firestore.batch()
        for (document in snapshot.documents) {
            batch.delete(document.reference)
        }

        batch.commit().await()
    }

    suspend fun deleteCartItem(cartId: Int, cartItemId: Int) {
        cartCollectionRef
            .document(cartId.toString())
            .collection("cart_items")
            .document(cartItemId.toString())
            .delete()
            .await()
    }

}