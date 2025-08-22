package com.example.quanlybandienthoai.view.screens.product


import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.repository.CartRepository
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import kotlin.math.ceil

//            val detailScreen = Screen.ProductDetail(phoneData.id)
//            viewModel.selectScreen(detailScreen) // Thêm màn hình chi tiết vào danh sách
//            navController.navigate("productDetail/${phoneData.id}")
//            navController.navigate(detailScreen.route)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductListCard(
    product: Sanpham,
    phienbansanphamList: List<Phienbansanpham>,
    navToProduct: () -> Unit,
    highlights: String,
    viewModel: HomeViewModel,
    navController: NavController,
    cartViewModel: CartViewModel,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
) {

    val price_sale = remember { mutableStateOf(0.0) }
    val giaxuat = remember { mutableStateOf(0.0) }

    val mapbsp = remember { mutableStateOf(-1) }

    LaunchedEffect(phienbansanphamList) {
        if (phienbansanphamList.isNotEmpty()) {
            price_sale.value = phienbansanphamList[0].price_sale
            giaxuat.value = phienbansanphamList[0].giaxuat
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                navToProduct()
            }
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)) // Add shadow for depth effect
            .animateContentSize() // Add smooth resizing effect
    )
    {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ảnh sản phẩm
            val imageArray = product.hinhanh.split(",").map { it.trim() }

            Image(
                painter = rememberAsyncImagePainter(imageArray[0]),
                contentDescription = product.tensp,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Tên sản phẩm
                Text(
                    text = product.tensp,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(phienbansanphamList) { phienban ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, RoundedCornerShape(7.dp))
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        phienban.rom?.let {
                                            viewModel.getPriceByRom(
                                                product.masp,
                                                phienban.maphienbansp,
                                                it.toInt()
                                            ) { (tem1, tem2) ->
                                                price_sale.value = tem1.toDouble()
                                                giaxuat.value = tem2.toDouble()
                                            }

                                            mapbsp.value = phienban.maphienbansp
                                        }
                                    }
                                }
                        ) {
                            Text(
                                text = if ((phienban.rom?.toInt() ?: 0) == 1000) {
                                    "1 TB"
                                } else {
                                    "${phienban.rom} GB"
                                },
                                fontSize = 11.sp, color = Color.Black
                            )
                        }
                    }
                }

                // Giá sản phẩm
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatCurrency(price_sale.value),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    if (price_sale.value != giaxuat.value) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatCurrency(giaxuat.value),
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Xếp hạng sao
                val rating = product.soluongdanhgia.takeIf { it > 0 }
                    ?.let { ceil((product.tongsao / it).toDouble()).toInt() } ?: 0

                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (rating >= i) {
                                Icons.Default.Star
                            } else {
                                Icons.Default.StarBorder
                            },
                            contentDescription = "Rating Star",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.soluongdanhgia} đánh giá",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(modifier = Modifier.fillMaxHeight()) {
//                IconButton(
//                    onClick = { /* TODO: Thêm vào danh sách yêu thích */ },
//                    modifier = Modifier
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.FavoriteBorder,
//                        contentDescription = "Favorite",
//                        tint = Color.Gray
//                    )
//                }

                IconButton(
                    onClick = {
                        cartViewModel.checkCartIdKH(
                            appViewModel.getCurrentUserId().toInt()
                        ) { exist ->
                            Log.d("CartCheck", "Cart exist: $exist")

                            val cartId = cartViewModel.cartId.value
                            Log.d("CartId", "Cart ID: $cartId")

                            var cartItem = CartItem(
                                -1,
                                -1,
                                phienbansanphamList[0].maphienbansp,
                                product.masp,
                                1
                            )

                            var pb = phienbansanphamList[0]

                            if (phienbansanphamList[0].price_sale != price_sale.value && mapbsp.value != -1) {
                                cartItem = CartItem(-1, -1, mapbsp.value, product.masp, 1)

                                phienbansanphamList.forEach { phienban ->
                                    if (phienban.maphienbansp == mapbsp.value) {
                                        pb = phienban
                                    }
                                }
                            }

                            if (exist) {
                                cartViewModel.insertCartItem(cartId, cartItem, product, pb)
                            } else {
                                cartViewModel.insertCart(
                                    Cart(
                                        -1,
                                        appViewModel.getCurrentUserId().toInt()
                                    ), cartItem, product, pb
                                )
                            }
                        }
//
//                        var pb = phienbansanphamList[0]
//
//                        if (phienbansanphamList[0].price_sale != price_sale.value && mapbsp.value != -1) {
//
//                            phienbansanphamList.forEach { phienban ->
//                                if (phienban.maphienbansp == mapbsp.value) {
//                                    pb = phienban
//                                }
//                            }
//                        }
//
//                        cartViewModel.addToCart(product, pb, 1)
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}
