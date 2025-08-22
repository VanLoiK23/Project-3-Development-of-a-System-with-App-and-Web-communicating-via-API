package com.example.quanlybandienthoai.view.screens.profile.order

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.OrderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavHostController,
    id: Int,
    viewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }

    reviewText = ""
    LaunchedEffect(Unit) {
        viewModel.getOrderDetails(id)
    }

    val currentStatus = when (viewModel.order?.status) {
        0 -> {
            OrderStatusStep.CONFIRMED
        }

        1, 2 -> {
            OrderStatusStep.WAITING
        }

        3 -> {
            OrderStatusStep.DELIVERING
        }

        4 -> {
            OrderStatusStep.REVIEW
        }

        else -> {
            null
        }
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
    }, bottomBar = {
        if (currentStatus == OrderStatusStep.CONFIRMED) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        showSheet = true
                        scope.launch {
                            sheetState.show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.order?.status == -2) {
                            Color(0xFFB2FF59)
                        } else {
                            Color(0xFFFF5722)
                        }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (viewModel.order?.status == -2) "Đã gửi đơn hủy" else "Hủy đơn hàng",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        } else if (currentStatus == null) {
            if (viewModel.order?.status == -2) {
                reviewText = viewModel.order?.feeback!!
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (viewModel.order?.status == -2) {
                            showSheet = true
                            scope.launch {
                                sheetState.show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    enabled = if (viewModel.order?.status == -2) {
                        true
                    } else {
                        false
                    }
                ) {
                    Text(
                        text =
                        if (viewModel.order?.status == -2) {
                            "Xem đơn gửi của bạn"
                        } else if (viewModel.order?.status == -1) {
                            "Nhân viên đã hủy đơn hàng"
                        } else {
                            "Bạn đã hủy đơn hàng"
                        },
                        color = if (viewModel.order?.status == -2) Color.White else Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            //Status
            //OrderStatusTimeline(currentStep = OrderStatusStep.REVIEW)
            if (currentStatus != null) {
                StatusBar(currentStatus = currentStatus)
            }

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
                        if (viewModel.order?.status == 4) {
                            true
                        } else {
                            false
                        }
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
                                    "Vui lòng thanh toán ",
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


        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        showSheet = false
                    }
                },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text =
                        if (viewModel.order?.status == -2) {
                            "Lý do hủy đơn hàng"
                        } else {
                            "Nhập lý do hủy đơn hàng"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        placeholder = { Text("Viết lý do của bạn...") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Gray,
                            focusedBorderColor = Color(0xFFFF9800)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        if (viewModel.order?.status == -2) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        showSheet = false
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Xác nhận", color = Color.White)
                            }
                        } else {

                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        showSheet = false
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Huỷ")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = {
                                    viewModel.cancel(reason = reviewText)
                                    scope.launch {
                                        sheetState.hide()
                                        showSheet = false
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Gửi", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }


        SuccessDialog(
            openDialog = viewModel.isCancelSuccess,
            "Send success !!",
            "Gửi đơn hủy thành công",
            "Confirm"
        ) {
            viewModel.isCancelSuccess.value = false
        }
    }
}

enum class OrderStatusStep(val label: String, val icon: ImageVector) {
    CONFIRMED("Chờ Xác Nhận", Icons.Default.HourglassEmpty),
    WAITING("Chờ Lấy Hàng", Icons.Default.MarkunreadMailbox),
    DELIVERING("Đang Giao", Icons.Default.LocalShipping),
    REVIEW("Đánh Giá", Icons.Default.StarBorder)
}

@Composable
fun StatusBar(currentStatus: OrderStatusStep) {
    val steps = OrderStatusStep.entries.toTypedArray()
    val currentIndex = steps.indexOf(currentStatus) // Tìm trạng thái hiện tại
    val progress = remember { Animatable(0f) } // Giá trị hoạt ảnh

    // Hoạt ảnh chạy từ 0 đến trạng thái hiện tại
    LaunchedEffect(currentIndex) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = currentIndex.toFloat(),
            animationSpec = tween(durationMillis = 6000, easing = FastOutSlowInEasing)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(16.dp),
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
        horizontalArrangement = Arrangement.spacedBy((-3).dp)
    ) {
        for (i in steps.indices) {
            StatusStep(
                icon = steps[i].icon,
                label = steps[i].label,
                isActive = i <= progress.value,
                isCompleted = i < progress.value
            )
            if (i < steps.size - 1) {
                StatusLine(
                    isActive = i < progress.value,
                    animatedProgress = if (i < progress.value && i + 1 > progress.value) {
                        progress.value - i
                    } else if (i < progress.value) {
                        1f
                    } else {
                        0f
                    }
                )
            }
        }
    }
}

@Composable
fun StatusStep(icon: ImageVector, label: String, isActive: Boolean, isCompleted: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(if (isActive) MaterialTheme.colorScheme.primary else Color.LightGray)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) Color.White else Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

@Composable
fun StatusLine(isActive: Boolean, animatedProgress: Float) {
    val color = if (isActive) MaterialTheme.colorScheme.primary else Color.LightGray

    Canvas(
        modifier = Modifier
            .width(35.dp)
            .height(4.dp)
    ) {
        drawLine(
            color = color,
            strokeWidth = 8f,//Độ dày của đường
            cap = StrokeCap.Round,//Đầu của đường thẳng
            start = Offset(0f, size.height / 2),
            end = Offset(size.width * animatedProgress, size.height / 2)
        )
    }
}


