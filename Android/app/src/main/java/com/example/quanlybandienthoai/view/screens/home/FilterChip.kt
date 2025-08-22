package com.example.quanlybandienthoai.view.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlybandienthoai.viewmodel.FilterViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel


@Composable
fun FilterScreen(
    filterViewModel: FilterViewModel,
    homeViewModel: HomeViewModel
) {
    val scroll = rememberScrollState()

    val filteredProducts by filterViewModel.filteredProducts.collectAsState()
    val sl by filterViewModel.selectedFilters.collectAsState()



    LaunchedEffect(homeViewModel.isFilter) {
        if (homeViewModel.isFilter && homeViewModel.getProductList().isEmpty()) {
            homeViewModel.getAllProducts()
        }
    }

    // Chỉ khởi tạo danh sách ban đầu nếu chưa được set
    LaunchedEffect(homeViewModel.getProductList()) {
        if (filterViewModel.productsInit.isEmpty() && homeViewModel.getProductList().isNotEmpty()) {
            filterViewModel.productsInit = homeViewModel.getProductList().toList()
            filterViewModel.applyFilters() // Áp dụng bộ lọc ngay
        }
    }

    // Nếu có danh sách lọc, cập nhật lại danh sách sản phẩm trong HomeViewModel
//    LaunchedEffect(filteredProducts) {
//        if (filteredProducts.isNotEmpty() && homeViewModel.isFilter) {
//            homeViewModel.setProductList(filteredProducts)
//        }
//    }
    LaunchedEffect(filteredProducts) {
        if (filteredProducts.isNotEmpty() && homeViewModel.isFilter && homeViewModel.getProductList() != filteredProducts) {
            homeViewModel.setProductList(filteredProducts)
        }
    }



    Column(modifier = Modifier.padding(16.dp)) {
        // Vùng chứa các tiêu chí lọc
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scroll),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterViewModel.filters.take(filterViewModel.filters.size-2).forEach { (category, options) ->
                FilterCategoryButton(category, options, filterViewModel)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị các bộ lọc đã chọn
        val selectedFilters by filterViewModel.selectedFilters.collectAsState()

        AnimatedVisibility(visible = selectedFilters.isNotEmpty()) {
            SelectedFilters(filterViewModel)
        }
    }
}

// Nút mở menu dropdown với animation
@Composable
fun FilterCategoryButton(
    category: String,
    options: List<String>,
    filterViewModel: FilterViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF37474F)), // Xám xanh hiện đại
            modifier = Modifier.shadow(4.dp, RoundedCornerShape(8.dp))
        ) {
            Text(category, color = Color.White, fontSize = 14.sp)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
//                    selectedFilters[category] = option
                    filterViewModel.selectFilter(category = category, value = option)

                    expanded = false
                })
            }
        }
    }
}

// Hiển thị filter đã chọn với hiệu ứng animation
@Composable
fun SelectedFilters(
    filterViewModel: FilterViewModel
) {
    val selectedFilters by filterViewModel.selectedFilters.collectAsState()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Button(
                onClick = {
                    filterViewModel.clearAll()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)) // Đỏ đẹp hơn
            ) {
                Text("Xóa bộ lọc", color = Color.White, fontSize = 14.sp)
            }
        }

        selectedFilters.forEach { (category, option) ->
            item {
                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .background(
                            Color(0xFF42A5F5),
                            shape = RoundedCornerShape(16.dp)
                        ) // Màu xanh nhẹ
                        .clickable {
                            filterViewModel.clearFilter(
                                category
                            )
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "$option ✖",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

