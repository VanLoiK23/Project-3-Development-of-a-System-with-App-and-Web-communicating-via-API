package com.example.quanlybandienthoai.view.screens.home

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.view.screens.home.FilterScreen
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import kotlin.math.ceil


@Composable
fun ProductHighlights(
    highlight: String,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartViewModel: CartViewModel
) {

    val sanphamList by remember {
        derivedStateOf {
            homeViewModel.highlightProductsMap[highlight]?.take(
                10
            )
        }
    }

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
                .background(homeViewModel.getColor(highlight), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = highlight.uppercase(Locale.getDefault()),
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily()
            )
        }

        TextButton(onClick = {
            homeViewModel.setColor(highlight, Color.Black)

            homeViewModel.titleList = highlight
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

    LazyRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        if (sanphamList != null) {
            items(sanphamList!!) { product: Sanpham ->
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
                                    navController.navigate("productDetail/${product.masp}")
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
                                navController.navigate("productDetail/${product.masp}")
                            },
                            highlights = highlight,
                            viewModel = homeViewModel,
                            cartViewModel
                        )
                    }
                }
            }
        }
    }



}


