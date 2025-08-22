package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.SearchBarState
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.repository.BrandRepository
import com.example.quanlybandienthoai.model.repository.CartRepository
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(val context: Application) : AndroidViewModel(context) {
    private val productRepository = ProductRepository
    private val categoryRepository = BrandRepository
    var filteredProductList = mutableStateListOf<Sanpham>()
        private set

    private val prefs = context.getSharedPreferences("prefs", MODE_PRIVATE)

    var scrollPosition = MutableStateFlow(prefs.getInt("scroll_position", 0))
        private set

    fun updateScrollPosition(position: Int) {
        scrollPosition.value = position
        prefs.edit().putInt("scroll_position", position).apply()
    }

    var colorListEqualRom = mutableStateListOf<String>()

    var mapbsp = mutableStateOf<Int>(-1)


    var actionType by mutableStateOf("")
    val highlightList = listOf(
        "🔥 Nổi bật nhất",
        "🆕 Sản phẩm mới",
        "💳 Trả góp 0 %",
        "💥 Giá sốc online",
        "🔻 Giảm giá lớn",
        "🏠 Giá rẻ cho mọi nhà"
    )

    var isLoading by mutableStateOf(false)

    var isFilter by mutableStateOf(false)

    val _noResultsMessage = MutableStateFlow("")
    val noResultsMessage: StateFlow<String> = _noResultsMessage

    var titleList by mutableStateOf("")
    var titleListCurrent by mutableStateOf("")


    private val initialColor = Color.Red
    val list = getColorsList().toMutableStateList()

    fun setColor(highlight: String, color: Color) {
        val index = highlightList.indexOf(highlight)
        list[index] = color
    }

    private fun getColorsList(): List<Color> {
        return highlightList.map { initialColor }
    }

    fun getColor(highlight: String): Color {
        val index = highlightList.indexOf(highlight)
        return list[index]
    }

//    fun getLikeProducts(query: String): List<Sanpham> {
//        var searchItems: List<Sanpham> = listOf()
//        runBlocking {
//            this.launch(Dispatchers.IO) {
//                val products = productRepository.getProductByName(query)
//                searchItems = products
//            }
//        }
//        return searchItems
//    }


    fun setProductList(list: List<Sanpham>) {
        filteredProductList.clear()
        filteredProductList.addAll(list)
    }

    fun getProductList(): List<Sanpham> {
        return filteredProductList.toList() ?: emptyList()
    }

    var categories by mutableStateOf<List<Thuonghieu>>(emptyList())
        private set
    var isLoadingBrand by mutableStateOf(false)


    fun getAllCategories() {
        isLoadingBrand = true
        viewModelScope.launch {
            categories = categoryRepository.getAllCategories()

//            val carts = CartRepository.getAllCarts(5)
//            val users = UserRepository.getUserById(5)
//
//            Log.d("AAA",(carts+users).toString())
        }
        isLoadingBrand = false
    }

//    fun getCategoryProducts(query: String) {
//        isFilter = true
//        isLoading = true
//        viewModelScope.launch {
//            val products = productRepository.getProductsByBrand(query.toInt())
//            if(products.isNotEmpty()){
//                setProductList(products)
//                isLoading = false
//            }
//        }
//    }

    private val _phienBanMap = mutableStateMapOf<Int, List<Phienbansanpham>>()
    val phienBanMap: Map<Int, List<Phienbansanpham>> get() = _phienBanMap

    fun getPhienbansanpham(masp: Int) {
        if (_phienBanMap.containsKey(masp)) return // Nếu đã có, không cần gọi lại
        viewModelScope.launch {
            val list = productRepository.getAllPhienBanSanPham(masp)
            _phienBanMap[masp] = list // Lưu vào ViewModel
        }
    }

    fun getValuePromo(promoName: String): Double {
        var value = 0.0
        viewModelScope.launch {
            val temp = productRepository.getValuePromo(promoName)
            value = temp
        }

        return value
    }

    fun getListColor(masp: Int, mapb: Int) {
        viewModelScope.launch {
            val colors = productRepository.getColorByMapb(masp, mapb)
            colorListEqualRom.clear()  // Xóa dữ liệu cũ
            colorListEqualRom.addAll(colors)  // Thêm dữ liệu mới
            Log.d("getListColor", "Updated colorListEqualRom: $colorListEqualRom")
        }
    }

//    fun getMaPBSP(masp: Int, rom: String,ram:String,color:String) {
//        viewModelScope.launch {
//            val id = productRepository.getMaPBSPByRomRamMauSac(masp, rom,ram,color)
//
//            if(id!=null){
//                mapbsp.value= id.toInt()
//            }
//            Log.d("Mapbsp", "Updated mapbsp: $id")
//        }
//    }


    fun getPriceByRom(
        masp: Int,
        maphienbansp: Int,
        rom: Int,
        onSuccess: (Pair<Float, Float>) -> Unit
    ) {
        viewModelScope.launch {
            val (temp1, temp2) = productRepository.getPriceByRom(masp, maphienbansp, rom.toString())
            if (temp1 > 0 && temp2 > 0) {
                onSuccess(Pair(temp1, temp2))
            }
        }
    }

    fun insertSanpham(sanpham: Sanpham, phienbansanphamList: List<Phienbansanpham>) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                productRepository.insertProduct(sanpham)

                val maspCurrent = productRepository.getNextProductId() - 1

                phienbansanphamList.forEach { phienbansanpham: Phienbansanpham ->
                    productRepository.insertPhienBanSanPham(maspCurrent, phienbansanpham)
                }
            }
        }
    }

    fun insertCategory(brand: Thuonghieu) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                categoryRepository.insertCategory(brand)
            }
        }
    }


    var highlightProducts by mutableStateOf<List<Sanpham>>(emptyList())
        private set

    private val _highlightProductsMap = mutableStateMapOf<String, List<Sanpham>>()

    val highlightProductsMap: Map<String, List<Sanpham>> get() = _highlightProductsMap

    fun getAllProducts() {
        viewModelScope.launch {
            setProductList(productRepository.getAllProducts())
        }
    }

    fun getProductsByHighlights(highlight: String) {
        isLoading = true
        viewModelScope.launch {
//            highlightProducts =
            when (highlight) {
                "🔥 Nổi bật nhất" -> {
                    setColor(highlight, Color(0xFFFF4D00))

                    _highlightProductsMap[highlight] = productRepository.getTopRatedProducts()
                }

                "🆕 Sản phẩm mới" -> {
                    setColor(highlight, Color(0xFF42BCF4))

                    _highlightProductsMap[highlight] = productRepository.getNewArrivals()
                }

                "💳 Trả góp 0 %" -> {
                    setColor(highlight, Color(0xFFFF4D00))

                    _highlightProductsMap[highlight] = productRepository.getInstallmentProducts()
                }

                "💥 Giá sốc online" -> {
                    setColor(highlight, Color(0xFF5DE272))

                    _highlightProductsMap[highlight] =
                        productRepository.getProductsByPromo("giareonline")
                }

                "🔻 Giảm giá lớn" -> {
                    setColor(highlight, Color(0xFFFF4D00))

                    _highlightProductsMap[highlight] =
                        productRepository.getProductsByPromo("giamgia")
                }

                "🏠 Giá rẻ cho mọi nhà" -> {
                    setColor(highlight, Color(0xFF5DE272))

                    _highlightProductsMap[highlight] = productRepository.getCheaperProducts()
                }

//                else -> emptyList()
            }
            isLoading = false
        }
    }


    private val _searchBarState: MutableState<SearchBarState> =
        mutableStateOf(value = SearchBarState.CLOSED)
    val searchBarState: State<SearchBarState> = _searchBarState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchBarState(newValue: SearchBarState) {
        _searchBarState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }
}

class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
