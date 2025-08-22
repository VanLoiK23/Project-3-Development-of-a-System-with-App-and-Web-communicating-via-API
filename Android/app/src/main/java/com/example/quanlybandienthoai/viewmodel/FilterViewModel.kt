package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.round

class FilterViewModel(val context: Application, private val homeViewModel: HomeViewModel) :
    AndroidViewModel(context) {

    // Danh sách sản phẩm gốc (từ API hoặc database)
    // Danh sách bộ lọc có thể áp dụng
    var filters = listOf(
        "Giá tiền" to listOf(
            "Dưới 2 triệu",
            "Từ 2 - 4 triệu",
            "Từ 4 - 7 triệu",
            "Từ 7 - 13 triệu",
            "Trên 13 triệu"
        ),
        "Khuyến mãi" to listOf("Giảm giá", "Trả góp", "Mới ra mắt", "Giá rẻ online"),
        "Số lượng sao" to listOf("Trên 2 sao", "Trên 3 sao", "Trên 4 sao"),
//        "Bộ nhớ" to listOf("32GB", "64GB", "128GB", "256GB", "512GB"),
        "Sắp xếp" to listOf(
            "Giá tăng dần",
            "Giá giảm dần",
            "Sao tăng dần",
            "Sao giảm dần",
            "Đánh giá tăng dần",
            "Đánh giá giảm dần",
            "Tên A-Z",
            "Tên Z-A"
        ),
        "Thương hiệu" to homeViewModel.categories.map { thuonghieu -> thuonghieu.tenthuonghieu },
        "Tên sản phẩm" to listOf(homeViewModel.searchTextState.value)
    )
        private set

    var productsInit by mutableStateOf<List<Sanpham>>(emptyList())

    private val _selectedFilters = MutableStateFlow<Map<String, String>>(emptyMap())
    val selectedFilters: StateFlow<Map<String, String>> = _selectedFilters

    // Trạng thái danh sách sản phẩm đã lọc
    private val _filteredProducts = MutableStateFlow<List<Sanpham>>(productsInit)
    val filteredProducts: StateFlow<List<Sanpham>> = _filteredProducts


    // Hàm chọn bộ lọc
    fun selectFilter(category: String, value: String) {
        homeViewModel.titleListCurrent = "Tìm kiếm nâng cao"

        homeViewModel.isFilter = true
        viewModelScope.launch {
            _selectedFilters.update { filters ->
                filters.toMutableMap().apply { put(category, value) }
            }
            homeViewModel.isLoading = true
            applyFilters()
            homeViewModel.isLoading = false

        }
    }

    // Hàm bỏ chọn bộ lọc
    fun clearFilter(category: String) {
        viewModelScope.launch {
            _selectedFilters.update { filters ->
                filters.toMutableMap().apply { remove(category) }
            }

            if (_selectedFilters.value.isNotEmpty()) {
                homeViewModel.isLoading = true
                applyFilters()
                homeViewModel.isLoading = false
            } else {
                clearAll()
            }
        }
    }

    init {
        viewModelScope.launch {
            selectedFilters.collect {
                applyFilters()
            }
        }

    }

    fun clearAll() {
        viewModelScope.launch {
            _selectedFilters.update {
                emptyMap()
            }
        }

        homeViewModel.titleListCurrent = ""
        productsInit = emptyList()
        homeViewModel.isFilter = false
        homeViewModel.setProductList(emptyList())
    }

    // Hàm áp dụng bộ lọc lên danh sách sản phẩm
    fun applyFilters() {

        val filters = _selectedFilters.value
        val filteredList = productsInit.filter { product ->
            val brandMap = homeViewModel.categories.associate { brand ->
                brand.mathuonghieu to brand.tenthuonghieu
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -14)
            val twoWeeksAgo = calendar.time

            var matchesAll = true

            filters.forEach { (key, value) ->
                val firstPBSP = homeViewModel.phienBanMap[product.masp]
                    ?.minByOrNull { it.price_sale ?: Double.MAX_VALUE }?.price_sale ?: 0.0

                when (key) {
                    "Thương hiệu" -> matchesAll =
                        matchesAll && product.thuonghieu == brandMap.entries.find { it.value == value }?.key

                    "Tên sản phẩm" -> matchesAll =
                        matchesAll && product.tensp.contains(value, ignoreCase = true)


                    "Giá tiền" -> matchesAll = matchesAll && when (value) {
                        "Dưới 2 triệu" -> firstPBSP < 2_000_000.0
                        "Từ 2 - 4 triệu" -> firstPBSP in 2_000_000.0..4_000_000.0
                        "Từ 4 - 7 triệu" -> firstPBSP in 4_000_000.0..7_000_000.0
                        "Từ 7 - 13 triệu" -> firstPBSP in 7_000_000.0..13_000_000.0
                        "Trên 13 triệu" -> firstPBSP > 13_000_000.0
                        else -> true
                    }

                    "Khuyến mãi" -> matchesAll = matchesAll && when (value) {
                        "Giảm giá" -> product.promo.any {
                            it["name"] == "giamgia" && (it["value"]?.toString()?.toDoubleOrNull()
                                ?: 0.0) > 0.0
                        }

                        "Trả góp" -> product.promo.any {
                            it["name"] == "tragop" && (it["value"]?.toString()?.toDoubleOrNull()
                                ?: 0.0) == 0.0
                        }

//                        "Mới ra mắt" -> product.promo.any {
//                            it["name"] == "moiramat" && (it["value"]?.toString()?.toDoubleOrNull()
//                                ?: 0.0) == 0.0
//                        }
                        "Mới ra mắt" -> {
                            try {
                                val createdDate = product.created?.let { dateFormat.parse(it) }
                                createdDate?.after(twoWeeksAgo) == true
                            } catch (e: ParseException) {
                                false
                            }
                        }

                        "Giá rẻ online" -> product.promo.any {
                            it["name"] == "giareonline" && (it["value"]?.toString()
                                ?.toDoubleOrNull() ?: 0.0) > 0.0
                        }

                        else -> true
                    }

                    "Số lượng sao" -> {
                        val rating =
                            if (product.soluongdanhgia > 0) round(product.tongsao.toDouble() / product.soluongdanhgia) else 0.0
                        matchesAll = matchesAll && when (value) {
                            "Trên 2 sao" -> rating >= 2
                            "Trên 3 sao" -> rating >= 3
                            "Trên 4 sao" -> rating >= 4
                            else -> true
                        }
                    }
                }
            }
            matchesAll
        }


        if (filteredList.isEmpty()) {
            _filteredProducts.value = emptyList() // Đảm bảo cập nhật danh sách rỗng
            homeViewModel._noResultsMessage.value = "Không có kết quả phù hợp"
        } else {
            _filteredProducts.update { filteredList.toList() }

            homeViewModel._noResultsMessage.value = ""
        }

        if ((_selectedFilters.value.size == 1 && _selectedFilters.value.containsKey("Sắp xếp"))) {
            _filteredProducts.value = emptyList()
            homeViewModel._noResultsMessage.value = "Vui lòng chọn thêm tiêu chí lọc để sắp xếp"
        }
        // Áp dụng sắp xếp
        _selectedFilters.value["Sắp xếp"]?.let { sortOption ->
            _filteredProducts.update { list ->
                when (sortOption) {
                    "Giá tăng dần" -> list.sortedBy {
                        homeViewModel.phienBanMap[it.masp]?.firstOrNull()?.price_sale ?: Double.MAX_VALUE
                    }
                    "Giá giảm dần" -> list.sortedByDescending {
                        homeViewModel.phienBanMap[it.masp]?.firstOrNull()?.price_sale ?: Double.MIN_VALUE
                    }

                    "Sao tăng dần" -> list.sortedBy { if (it.soluongdanhgia > 0) it.tongsao.toDouble() / it.soluongdanhgia else 0.0 }
                    "Sao giảm dần" -> list.sortedByDescending { if (it.soluongdanhgia > 0) it.tongsao.toDouble() / it.soluongdanhgia else 0.0 }
                    "Đánh giá tăng dần" -> list.sortedBy { it.soluongdanhgia }
                    "Đánh giá giảm dần" -> list.sortedByDescending { it.soluongdanhgia }
                    "Tên A-Z" -> list.sortedBy { it.tensp }
                    "Tên Z-A" -> list.sortedByDescending { it.tensp }
                    else -> list
                }
            }
        }
    }


}

class FilterViewModelFactory(
    private val application: Application,
    private val homeViewModel: HomeViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilterViewModel(application, homeViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


