package com.example.quanlybandienthoai.view.screens.product

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.quanlybandienthoai.view.screens.home.ProductCard
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import kotlin.math.ceil


@Composable
fun ProductSimilar(
    highlight: String,
    id: Int,
    navController: NavController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    val sanphamList by remember(homeViewModel.titleListCurrent, highlight, id) {
        mutableStateOf(
            if (homeViewModel.titleListCurrent == "Tìm kiếm nâng cao") {
                homeViewModel.getProductList().take(10).filterNot { it.masp == id }
            } else {
                homeViewModel.highlightProductsMap[highlight]?.take(10)?.filterNot { it.masp == id }
                    ?: emptyList()
            }
        )
    }


    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 2.dp)
                .background(
                    when {
                        highlight.isEmpty() -> Color.Blue
                        else -> homeViewModel.getColor(highlight) ?: Color.Gray
                    }, shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Các sản phẩm tương tự",
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily()
            )
        }

    }

    LazyRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(sanphamList) { product: Sanpham ->
            val phienBanList = homeViewModel.phienBanMap[product.masp] ?: emptyList()

            LaunchedEffect(product.masp) {
                homeViewModel.getPhienbansanpham(product.masp)
            }

            if (phienBanList.isNotEmpty()) {
                val rating = product.soluongdanhgia.takeIf { it > 0 }
                    ?.let { ceil((product.tongsao / it).toDouble()).toInt() } ?: 0

                if (highlight == "🔥 Nổi bật nhất") {
                    if (rating >= 4.0) {
                        ProductCard(
                            product = product,
                            phienbansanphamList = phienBanList,
                            navToProduct = {
                                val detailScreen = Screen.ProductDetail(product.masp)
                                mainViewModel.selectScreen(detailScreen)

                                navController.navigate("productDetail/${product.masp}") {
                                    popUpTo("productDetail/{id}") { inclusive = true }
                                }

                                val detailScreenOld = Screen.ProductDetail(id)
                                mainViewModel.deleteScreenInBottom(detailScreenOld)

                                homeViewModel.colorListEqualRom.clear()
                                mainViewModel.updateSelectedScreen(navController)
//                                    navController.navigate("product/${product.masp}")
                            },
                            highlights = highlight,
                            viewModel = homeViewModel,
                            cartViewModel
                        )
                    }
                } else {

                    ProductCard(
                        product = product,
                        phienbansanphamList = phienBanList,
                        navToProduct = {
                            val detailScreen = Screen.ProductDetail(product.masp)
                            mainViewModel.selectScreen(detailScreen)

                            navController.navigate("productDetail/${product.masp}") {
                                popUpTo("productDetail/{id}") { inclusive = true }
                            }

                            homeViewModel.colorListEqualRom.clear()
                            val detailScreenOld = Screen.ProductDetail(id)

                            mainViewModel.deleteScreenInBottom(detailScreenOld)

                            mainViewModel.updateSelectedScreen(navController)
                        },
                        highlights = highlight,
                        viewModel = homeViewModel,
                        cartViewModel
                    )
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