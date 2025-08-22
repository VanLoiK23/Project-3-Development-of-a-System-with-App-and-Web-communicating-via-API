package com.example.quanlybandienthoai.view.screens.admin.order

import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.view.screens.profile.order.OrderItemCard
import com.example.quanlybandienthoai.view.screens.profile.order.OrderStatusStep
import com.example.quanlybandienthoai.view.screens.profile.order.ProductItem
import com.example.quanlybandienthoai.view.screens.profile.order.StatusBar
import com.example.quanlybandienthoai.view.screens.profile.order.generateOrderCode
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.OrderViewModel
import com.example.quanlybandienthoai.viewmodel.admin.OrderManageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailManageScreen(
    navController: NavHostController,
    id: Int,
    viewModel: OrderManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    LaunchedEffect(id) {
        viewModel.setCurrentOrder(id)
    }

    Scaffold(topBar = {
        TopAppBar(title = {

            Text(
                text = "🛍️ Chi tiết đơn hàng", fontSize = 24.sp,
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
    }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {



            if (viewModel.order != null) {
                // Địa chỉ nhận hàng
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.AddLocation,
                                contentDescription = "Outlined Favorite Icon",
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                            Text("Địa chỉ nhận hàng", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${viewModel.order?.name}", fontWeight = FontWeight.Medium)
                        Text("(+84) ${viewModel.order?.phone}")
                        Text(
                            "${viewModel.order?.address}", maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sản phẩm
                var productList by remember { mutableStateOf<List<ProductItem>>(emptyList()) }

                productList = emptyList()
                viewModel.order?.listctpx!!.forEach { ct ->

                    productList += ProductItem(
                        ct.maSP!!,
                        ct.tenSP!!,
                        ct.srcImage!!,
                        ct.config!!,
                        ct.donGia.toDouble(),
                        ct.soLuong
                    )
                }

                if (productList.isNotEmpty()) {
                    OrderItemCard(
                        navController,
                        orderId = generateOrderCode(viewModel.order?.id!!),
                        viewModel.order?.id!!,
                        isFavorite = true,
                        productList = productList,
                        totalPrice = viewModel.order?.tongTien!!.toDouble(),
                        isDetails = true,
                        onContactClick = { /* open chat */ },
                        false
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                //phi van chuyen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Phí vận chuyển", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    if (viewModel.order?.feeTransport == 0) {
                        Text("Miễn phí", fontSize = 16.sp, color = Color.Green)
                    } else {
                        Text(
                            formatCurrency(200000.000),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))
                //discount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mã giảm giá", fontSize = 16.sp, fontWeight = FontWeight.Bold)


                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Red,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (viewModel.order?.infoOrderDiscount == null) {
                                    "Không"
                                } else {
                                    viewModel.order?.infoOrderDiscount!!.code
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                        }

                    }
                    Text(
                        if (viewModel.order?.infoOrderDiscount == null) {
                            "0"
                        } else {
                            formatCurrency(viewModel.order?.infoOrderDiscount!!.discountAmount.toDouble())
                        }, fontSize = 16.sp, color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                // Tổng tiền
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Thành tiền", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        formatCurrency(viewModel.order?.tongTien!!.toDouble()),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Phương thức thanh toán
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Phương thức thanh toán", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))

                        if (viewModel.order?.payment!!.isEmpty() || viewModel.order?.payment!!.isBlank()) {
                            Row {
                                Text(
                                    "Thanh toán ",
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    formatCurrency(viewModel.order?.tongTien!!.toDouble()),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                                Text(
                                    " khi nhận hàng",
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Text(
                                "Đã thanh toán bằng momo",
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}