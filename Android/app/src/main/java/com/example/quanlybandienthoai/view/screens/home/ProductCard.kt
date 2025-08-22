package com.example.quanlybandienthoai.view.screens.home

import android.text.Highlights
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import kotlin.math.ceil


//@Composable
//fun ProductCard(
//    product: Sanpham,
//    phienbansanpham: Phienbansanpham,
////    navToProduct: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .height(250.dp)
//            .requiredWidthIn(100.dp, 150.dp),
////        onClick = { navToProduct() },
//        //colors = CardDefaults.cardColors(Color.Gray)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                //.background(Color.White)
//                .padding(5.dp),
//            verticalArrangement = Arrangement.SpaceAround,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painter = rememberAsyncImagePainter(
//                    model =
//                    product.hinhanh
//                ),
//                contentDescription = "Product Image",
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    .background(Color.White)
//                    .height(150.dp)
//                    .width(150.dp)
//            )
//
//            Spacer(modifier = Modifier.height(5.dp))
//
//            Column() {
//                Row(
//                    Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        formatCurrency(phienbansanpham.giaxuat),
//                        style = TextStyle(
//                            fontFamily = FontFamily.Default,
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 16.sp,
//                        ),
//                        maxLines = 2,
//                        overflow = TextOverflow.Clip
//                    )
//                    Row() {
//                        Image(
//                            painter = painterResource(
//                                id = R.drawable.start
//                            ),
//                            contentDescription = "starRating",
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Text(
//                            formatCurrency(ceil((product.tongsao / product.soluongdanhgia).toDouble())),
//                            Modifier.padding(end = 5.dp),
//                            fontSize = 15.sp,
//                            fontWeight = FontWeight.Bold,
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(5.dp))
//
//                Text(
//                    product.tensp, style = TextStyle(
//                        fontFamily = FontFamily.Default,
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 16.sp,
//                    ), maxLines = 2, overflow = TextOverflow.Clip
//                )
//            }
//        }
//    }
//}


@Composable
fun ProductCard(
    product: Sanpham,
    phienbansanphamList: List<Phienbansanpham>,
    navToProduct: () -> Unit,
    highlights: String,
    viewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
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
            .width(210.dp)
            .padding(8.dp)
            .clickable { navToProduct() },
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Nhãn giảm giá
                if (highlights.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.TopStart),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        product.promo.forEach { promo ->
                            val name = promo["name"] as? String ?: ""

                            val value = when (val v = promo["value"]) {
                                is Int -> v
                                is Long -> v.toInt()
                                is String -> v.toIntOrNull() ?: 0
                                else -> 0
                            }

                            if (name.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = when (name) {
                                                "giamgia" -> Color(0xFFFF4D00) // Màu cam
                                                "moiramat" -> Color(0xFF42BCF4)  // Màu xanh
                                                "tragop" -> Color(0xFFFF4D00)
                                                "giareonline" -> Color.Green
                                                else -> Color.Green
                                            }, shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = when (name) {
                                            "giamgia" -> "Giảm tới ${formatCurrency(giaxuat.value * (value.toDouble() / 100))}🔥"
                                            "moiramat" -> "🆕 Sản phẩm mới"
                                            "tragop" -> "💳 Trả góp 0 %"
                                            "giareonline" -> "💥 Giá sốc online"
                                            else -> ""
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                } else {

                    if (highlights.contains("Giảm")) {
                        product.promo.forEach { promo ->
                            val name = promo["name"] as? String ?: ""


                            val value = when (val v = promo["value"]) {
                                is Int -> v
                                is Long -> v.toInt()
                                is String -> v.toIntOrNull() ?: 0
                                else -> 0
                            }

                            if (name.isNotBlank()) {
                                if (name.equals("giamgia")) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color(0xFFFF4D00), RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                            .align(Alignment.TopStart)
                                    ) {
                                        Text(
                                            text = "Giảm tới ${formatCurrency(giaxuat.value * (value.toDouble() / 100))}🔥",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (highlights.contains("Sản phẩm mới")) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFF42BCF4), RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Text(
                                text = highlights,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    if (highlights.contains("Trả")) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFFF4D00), RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Text(
                                text = highlights,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    if (highlights.contains("Giá sốc online")) {
                        Box(
                            modifier = Modifier
                                .background(Color.Green, RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Text(
                                text = "Giá rẻ online",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    if (highlights.contains("Nổi bật nhất")) {
                        Column(
                            modifier = Modifier.align(Alignment.TopStart),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            product.promo.forEach { promo ->
                                val name = promo["name"] as? String ?: ""

                                val value = when (val v = promo["value"]) {
                                    is Int -> v
                                    is Long -> v.toInt()
                                    is String -> v.toIntOrNull() ?: 0
                                    else -> 0
                                }

                                if (name.isNotBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = when (name) {
                                                    "giamgia" -> Color(0xFFFF4D00) // Màu cam
                                                    "moiramat" -> Color(0xFF42BCF4)  // Màu xanh
                                                    "tragop" -> Color(0xFFFF4D00)
                                                    "giareonline" -> Color.Green
                                                    else -> Color.Green
                                                }, shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = when (name) {
                                                "giamgia" -> "Giảm tới ${formatCurrency(giaxuat.value * (value.toDouble() / 100))}🔥"
                                                else -> highlights
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
                // Icon yêu thích
                IconButton(
                    onClick = { /* TODO: Thêm vào danh sách yêu thích */ },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Gray
                    )
                }
            }

            // Ảnh sản phẩm

            val imageArray = product.hinhanh.split(",").map { it.trim() }

            Image(
                painter = rememberAsyncImagePainter(imageArray[0]),
                contentDescription = product.tensp,
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tên sản phẩm
            Text(
                text = product.tensp,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tùy chọn bộ nhớ
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(phienbansanphamList) { phienban ->
                    Box(modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(7.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable {
                            coroutineScope.launch {
                                if (phienban.rom != null) {
                                    viewModel.getPriceByRom(
                                        product.masp,
                                        phienban.maphienbansp,
                                        phienban.rom!!.toInt()
                                    ) { (tem1, tem2) ->
                                        price_sale.value = tem1.toDouble()
                                        giaxuat.value = tem2.toDouble()
                                    }

                                    mapbsp.value = phienban.maphienbansp

                                }

                            }
                        }) {
                        Text(
                            text = if ((phienban.rom?.toIntOrNull() ?: 0) == 1000) {
                                "1 TB"
                            } else {
                                "${phienban.rom} GB"
                            }, fontSize = 10.sp, color = Color.Black
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(4.dp))

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

            val rating = product.soluongdanhgia.takeIf { it > 0 }
                ?.let { ceil((product.tongsao / it).toDouble()).toInt() } ?: 0


            // Xếp hạng sao và số đánh giá
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        for (i in 1..5 step (1)) {
                            if (rating >= i) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating Star",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(14.dp)
                                )
                            } else if (rating >= i - 0.5) {
                                Icon(
                                    imageVector = Icons.Default.StarHalf,
                                    contentDescription = "Rating Star",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(14.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.StarBorder,
                                    contentDescription = "Rating Star",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                        }


                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.soluongdanhgia} đánh giá",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

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
//
//                        cartViewModel.addToCart(product, pb, 1)


                    }, modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Favorite",
                        tint = Color.Gray
                    )
                }
            }

        }
    }
}

