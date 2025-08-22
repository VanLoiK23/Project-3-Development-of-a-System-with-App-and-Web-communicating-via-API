package com.example.quanlybandienthoai.view.screens.home

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import java.util.Locale


@Composable
fun ProductGridFilter(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    cartViewModel: CartViewModel
) {
    val sanphamList by remember { derivedStateOf { homeViewModel.getProductList() } }
    val noResultsMessage by homeViewModel.noResultsMessage.collectAsState()

//    FilterBar()
    if (noResultsMessage.isNotEmpty()) {
        Text(
            text = noResultsMessage,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
            color = Color.Red
        )
    } else {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//        Text(
//            text = highlight,
//            fontWeight = FontWeight.Bold,
//            fontSize = 18.sp,
//            modifier = Modifier
//                .padding(5.dp)
//                .offset(x = 5.dp),
//            color = homeViewModel.getColor(highlight),
//            textAlign = TextAlign.Left
//        )

            Box(
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, bottom = 2.dp)
                    .background(Color(0xFF42BCF4), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Tìm kiếm nâng cao",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 9.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamily()
                )
            }

            TextButton(onClick = {

                homeViewModel.titleList = "Tìm kiếm nâng cao"

                mainViewModel.selectScreen(Screen.Shop)
                navController.navigate(Screen.Shop.route)
//            navController.navigate("productList/${highlight}")
                homeViewModel.actionType = "Xem tất cả"
            }) {
                Text(
                    text = "Xem tất cả",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Right,
                    color = Color.Gray
                )
            }
        }

        // Grid hiển thị sản phẩm (2 cột)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 1000.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            state = rememberLazyGridState()
        ) {
            items(sanphamList) { product ->

                val phienBanList = homeViewModel.phienBanMap[product.masp] ?: emptyList()

                LaunchedEffect(product.masp) {
                    homeViewModel.getPhienbansanpham(product.masp)
                }

                if (phienBanList.isNotEmpty()) {
                    ProductCard(
                        product = product,
                        phienbansanphamList = phienBanList,
                        navToProduct = {
                            val detailScreen = Screen.ProductDetail(product.masp)
                            mainViewModel.selectScreen(detailScreen)
                            navController.navigate("productDetail/${product.masp}")
                        },
                        highlights = "",
                        viewModel = homeViewModel,
                        cartViewModel
                    )
                }


            }
        }

        val openRemoveCartItemDialog = remember { mutableStateOf(false) }



        if (cartViewModel.message.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = {
                    openRemoveCartItemDialog.value = false
                    cartViewModel.message = ""
                },
                title = {
                    if (cartViewModel.message == "Thêm sản phẩm vào giỏ hàng thành công") {
                        Text(text = "Thêm vào giỏ thành công !!")
                    } else {
                        Text(text = cartViewModel.message)
                    }
                },
                text = {
                    cartViewModel.message
                },
                confirmButton = {
                    if (cartViewModel.message == "Thêm sản phẩm vào giỏ hàng thành công") {
                        Button(
                            onClick = {
                                openRemoveCartItemDialog.value = false
                                cartViewModel.message = ""
                                navController.navigate(Screen.Cart.route)

                                mainViewModel.updateSelectedScreen(navController)
                            }
                        ) {
                            Text("Tới giỏ hàng")
                        }
                    } else {
                        Button(
                            onClick = {
                                openRemoveCartItemDialog.value = false
                                cartViewModel.message = ""

                            }
                        ) {
                            Text("Xác nhận")
                        }
                    }

                },
                dismissButton = {
                    Button(
                        onClick = {
                            openRemoveCartItemDialog.value = false
                            cartViewModel.message = ""
                        }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
}
//
//@Composable
//fun FilterBar() {
//    val filters = listOf(
//        "Giá tiền" to listOf("Dưới 2 triệu", "Từ 2 - 4 triệu", "Từ 4 - 7 triệu"),
//        "Khuyến mãi" to listOf("Có khuyến mãi", "Không khuyến mãi"),
//        "Số lượng sao" to listOf("Từ 3 sao", "Từ 4 sao", "Từ 5 sao"),
//        "Sắp xếp" to listOf("Giá tăng dần", "Giá giảm dần", "Bán chạy nhất")
//    )
//
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(13.dp)
//    ) {
//        filters.forEach { (title, options) ->
//            FilterDropdown(title, options)
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FilterDropdown(title: String, options: List<String>) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf(title) }
//
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = it }
//    ) {
//        Button(
//            onClick = {
//
//                    expanded = true
//
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//            modifier = Modifier
//                .clip(RoundedCornerShape(12.dp)) // Bo góc đẹp hơn
//        ) {
//            Text(selectedText, color = Color.White)
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier
//                .background(Color.White, shape = RoundedCornerShape(12.dp))
//                .shadow(8.dp) // Hiệu ứng nổi
//                .animateContentSize() // Trượt xuống mượt mà
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option, fontSize = 16.sp) },
//                    onClick = {
//                        selectedText = option
//                        expanded = false
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                )
//            }
//        }
//    }
//}
