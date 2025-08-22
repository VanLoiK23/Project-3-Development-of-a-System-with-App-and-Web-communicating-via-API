package com.example.quanlybandienthoai.view.screens.product

import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.TopBar
import com.example.quanlybandienthoai.view.screens.home.HomeTopBar
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModelFactory
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.SlideViewModel
import java.util.Locale
import kotlin.math.ceil

@Composable
fun ProductList(
    navController: NavController,
    homeViewModel: HomeViewModel,
    context: Application = LocalContext.current.applicationContext as Application,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    val listState = rememberLazyListState()

//    val sanphamList by remember {
//        derivedStateOf {
//            if (homeViewModel.titleList == "Tìm kiếm nâng cao") {
//                homeViewModel.getProductList()
//            } else {
//                homeViewModel.highlightProductsMap[homeViewModel.titleList]
//            }
//        }
//    }
//
//    val sanphamList by remember {
//        derivedStateOf {
//            when {
//                homeViewModel.titleList == "Tìm kiếm nâng cao" -> homeViewModel.getProductList()
//                homeViewModel.titleList.isEmpty() -> {
//                    homeViewModel.highlightList.flatMap { category ->
//                        homeViewModel.highlightProductsMap[category] ?: emptyList()
//                    }
//                }
//
//                else -> homeViewModel.highlightProductsMap[homeViewModel.titleList] ?: emptyList()
//            }
//        }
//    }


    val sanphamMap: Map<String, List<Sanpham>> by remember {
        derivedStateOf {
            if (homeViewModel.titleList.isEmpty()) {
                // Lấy danh sách tất cả sản phẩm theo từng promo
                homeViewModel.highlightList.associateWith { category ->
                    homeViewModel.highlightProductsMap[category] ?: emptyList()
                }
            } else if (homeViewModel.titleList == "Tìm kiếm nâng cao") {
                mapOf(
                    homeViewModel.titleList to (homeViewModel.getProductList())
                )
            } else {
                // Chỉ lấy danh sách sản phẩm của promo hiện tại
                mapOf(
                    homeViewModel.titleList to (homeViewModel.highlightProductsMap[homeViewModel.titleList]
                        ?: emptyList())
                )
            }
        }
    }


//    val sanphamList by remember { derivedStateOf { homeViewModel.highlightProductsMap[homeViewModel.titleList] } }


    Scaffold(
        topBar = {
            TopBar(homeViewModel.titleList, false, navBack = {
                navController.popBackStack()
                homeViewModel.titleList = ""

                mainViewModel.updateSelectedScreen(navController)
            })

        }, bottomBar = {
            BottomNavigationBar(navController, mainViewModel)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                sanphamMap.forEach { (category, sanphamList) ->
                    // Hiển thị tiêu đề của category
                    item {
                        Box(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 10.dp, bottom = 2.dp)
                                .background(
                                    if (homeViewModel.titleList == "") {
                                        homeViewModel.getColor(category)
                                    } else {
                                        Color(0xFF42BCF4)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = category.uppercase(Locale.getDefault()),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontFamily = fontFamily()
                            )
                        }
                    }

                    // Hiển thị danh sách sản phẩm của category này
                    items(sanphamList) { product: Sanpham ->
                        val phienBanList = homeViewModel.phienBanMap[product.masp] ?: emptyList()

                        LaunchedEffect(product.masp) {
                            homeViewModel.getPhienbansanpham(product.masp)
                        }

                        if (phienBanList.isNotEmpty()) {
                            val rating = product.soluongdanhgia.takeIf { it > 0 }
                                ?.let { ceil((product.tongsao / it).toDouble()).toInt() } ?: 0

                            if (category == "🔥 Nổi bật nhất" && rating < 4.0) return@items

                            ProductListCard(
                                product = product,
                                phienbansanphamList = phienBanList,
                                navToProduct = {
                                    val detailScreen = Screen.ProductDetail(product.masp)
                                    mainViewModel.selectScreen(detailScreen)
                                    navController.navigate("productDetail/${product.masp}")
//                                    navController.navigate(detailScreen.route)
                                    //       navController.navigate("product/${product.masp}")
                                },
                                highlights = category,
                                viewModel = homeViewModel,
                                navController,
                                cartViewModel
                            )
                        }
                    }
                }
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
