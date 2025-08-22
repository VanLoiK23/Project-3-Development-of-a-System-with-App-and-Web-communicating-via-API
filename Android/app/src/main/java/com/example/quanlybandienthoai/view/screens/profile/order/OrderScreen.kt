package com.example.quanlybandienthoai.view.screens.profile.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.OrderViewModel
import kotlinx.coroutines.launch


//display order
data class ProductItem(
    val masp: Int,
    val name: String,
    val image: String,
    val config: String,
    val price: Double,
    val quantity: Int
)

@Composable
fun OrderStatusTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    badgeCounts: List<Int> = emptyList(),
    modifier: Modifier
) {
    val tabs = listOf(
        "Chờ xác nhận" to Icons.Default.HourglassEmpty,
        "Chờ lấy hàng" to Icons.Default.MarkunreadMailbox,
        "Đang giao" to Icons.Default.LocalShipping,
        "Đánh giá" to Icons.Default.StarBorder,
        "Đã hủy" to Icons.Default.Cancel
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, (label, icon) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 8.dp)
            ) {
                Box {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (index == selectedTab) Color(0xFFFF5722) else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                    if (badgeCounts.size == 5) {
                        badgeCounts[index].takeIf { it > 0 }?.let { count ->
                            Box(
                                modifier = Modifier
                                    .offset(x = 14.dp, y = (-6).dp)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = count.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (index == selectedTab) FontWeight.Bold else FontWeight.Normal,
                    color = if (index == selectedTab) Color(0xFFFF5722) else Color.Gray
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OrderItemCard(
    navController: NavHostController,
    orderId: String,
    orderIdInteger: Int,
    isFavorite: Boolean,
    productList: List<ProductItem>,
    totalPrice: Double,
    isDetails: Boolean,
    onContactClick: () -> Unit,
    isReview: Boolean,
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = "Mã đơn hàng: $orderId",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isFavorite) {
                        Box(
                            modifier = Modifier
                                .background(Color.Red, shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("Yêu thích", color = Color.White, fontSize = 10.sp)
                        }
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Column {
                productList.forEach { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(product.image),
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Text(
                                product.config,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 10.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("x${product.quantity}", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                formatCurrency(product.price),
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isReview) {
                            LaunchedEffect(orderId, product.masp) {
                                viewModel.isReviewExist(orderIdInteger, product.masp)
                            }

                            val isReviewed = viewModel.reviewMap[product.masp] ?: false

                            Spacer(Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    navController.navigate("account/order/review/${orderIdInteger}/${product.masp}")
                                    viewModel.tensp = product.name
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFFF5722
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                if (isReviewed) {
                                    Text("Cập nhật đánh giá", fontSize = 12.sp, color = Color.White)
                                } else {
                                    Text("Đánh giá", fontSize = 12.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            if (!isDetails) {
                Spacer(Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("Thành tiền: ", fontSize = 12.sp)
                    Text(
                        formatCurrency(totalPrice),
                        fontSize = 14.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }


                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onContactClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Xem chi tiết", color = Color.White)
                }
            }
        }
    }
}

fun generateOrderCode(orderId: Int): String {
    return "DH" + orderId.toString().padStart(5, '0')
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(
    navController: NavHostController,
    viewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    LaunchedEffect(Unit) {
        viewModel.userId = appViewModel.getCurrentUserId().toInt()

        viewModel.getAllPhieuXuat()
    }
    Scaffold(topBar = {
        TopAppBar(title = {

            Text(
                text = "🛍️ Đơn hàng của bạn", fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily()
            )

        }, navigationIcon = {
            IconButton(onClick = {

                navController.popBackStack()

                mainViewModel.updateSelectedScreen(navController)
            }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFFF5722)
                )
            }
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
        )
    }) { padding ->


        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                Spacer(Modifier.height(10.dp))
            }

            if (viewModel.statusCount.isNotEmpty()) {
                stickyHeader {
                    Surface(
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OrderStatusTabs(
                            selectedTab = viewModel.selectedTab,
                            onTabSelected = {
                                viewModel.selectedTab = it
                                viewModel.getDetailOrders()
                            },
                            badgeCounts = viewModel.statusCount,
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(10.dp))
            }

            items(viewModel.ordersByTab) { order ->
                val productList = order.listctpx.map { ct ->
                    ProductItem(
                        name = ct.tenSP ?: "",
                        image = ct.srcImage ?: "",
                        config = ct.config ?: "",
                        price = ct.donGia.toDouble(),
                        quantity = ct.soLuong,
                        masp = ct.maSP!!
                    )
                }

                OrderItemCard(
                    navController,
                    orderId = generateOrderCode(order.id),
                    order.id,
                    isFavorite = true,
                    productList = productList,
                    totalPrice = order.tongTien.toDouble(),
                    isDetails = false,
                    onContactClick = {
                        navController.navigate("account/order/detail/${order.id}")
                    },
                    false,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}


