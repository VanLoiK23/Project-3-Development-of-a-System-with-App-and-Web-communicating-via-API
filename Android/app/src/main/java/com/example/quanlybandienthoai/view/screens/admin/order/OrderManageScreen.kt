package com.example.quanlybandienthoai.view.screens.admin.order

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.IdentifyDialog
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.screens.admin.sanpham.Pagination
import com.example.quanlybandienthoai.view.screens.admin.sanpham.TableCell
import com.example.quanlybandienthoai.view.screens.admin.sanpham.TableCellHeader
import com.example.quanlybandienthoai.view.screens.admin.sanpham.TableCellNameProduct
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.OrderManageViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderManagementScreen(
    navController: NavController,
    orderManageViewModel: OrderManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val pageSize = 7
    var currentPage by remember { mutableStateOf(1) }
    val allOrders by orderManageViewModel.orders.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    var selectedStatus by remember { mutableStateOf<Int?>(null) }
    var selectedTimeFilter by remember { mutableStateOf<String>("all") }

    var expandedStatus by remember { mutableStateOf(false) }
    var expandedTime by remember { mutableStateOf(false) }
//
//    val statusOptions = listOf(
//        "Tất cả trạng thái", // null
//        "Đang chờ duyệt",    // 0
//        "Đang lấy hàng",     // 1
//        "Đang chờ giao hàng",// 2
//        "Đang giao hàng",    // 3
//        "Đã giao",           // 4
//        "Nhân viên đã hủy",  // -1
//        "Khách hàng đã hủy"         // -2
//    )

    val statusOptions = mapOf(
        null to "Tất cả trạng thái",
        0 to "Đang chờ duyệt",
        1 to "Đang lấy hàng",
        2 to "Đang chờ giao hàng",
        3 to "Đang giao hàng",
        4 to "Đã giao",
        -1 to "Nhân viên đã hủy",
        -2 to "Khách hàng gửi đơn hủy",
        -3 to "Khách hàng đã hủy"
    )

    val timeOptions = mapOf(
        "all" to "Tất cả",
        "today" to "Hôm nay",
        "week" to "Tuần này",
        "month" to "Tháng này"
    )

    LaunchedEffect(Unit) {
        if (allOrders.isEmpty()) {
            isLoading = true
            orderManageViewModel.getOrders()
            isLoading = false
        }

        mainViewModel.updateSelectedScreen(navController)

    }

    // Chia trang từ list tổng
    val orders = remember(currentPage, allOrders) {
        val start = (currentPage - 1) * pageSize
        val end = (start + pageSize).coerceAtMost(allOrders.size)
        if (start in 0 until end) {
            allOrders.subList(start, end)
        } else {
            emptyList()
        }
    }

    val totalPages = (allOrders.size + pageSize - 1) / pageSize


    val onPageChange = rememberUpdatedState { selectedPage: Int ->
        if (currentPage != selectedPage) {
            isLoading = true
            currentPage = selectedPage
        }
    }

    LaunchedEffect(currentPage) {
        delay(500) // Giả lập thời gian tải
        isLoading = false
    }


    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Quản lý đơn hàng")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF7D32A8),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, mainViewModel)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                HeaderSectionOrder()

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Select Status
                        ExposedDropdownMenuBox(
                            modifier = Modifier.weight(2f),
                            expanded = expandedStatus,
                            onExpandedChange = { expandedStatus = !expandedStatus }
                        ) {
                            val selectedStatusName =
                                statusOptions[selectedStatus] ?: "Không xác định"

                            TextField(
                                modifier = Modifier.menuAnchor(),
                                value = selectedStatusName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Lọc theo trạng thái") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),

                                )
                            ExposedDropdownMenu(
                                expanded = expandedStatus,
                                onDismissRequest = { expandedStatus = false }
                            ) {
//                                statusOptions.forEachIndexed { index, status ->
//                                    DropdownMenuItem(
//                                        text = { Text(status) },
//                                        onClick = {
//                                            selectedStatus = if (index == 0) null else index - 1
//                                            expandedStatus = false
//                                        }
//                                    )
//                                }

                                statusOptions.forEach { (key, value) ->
                                    DropdownMenuItem(
                                        text = { Text(value) },
                                        onClick = {
                                            selectedStatus = key
                                            expandedStatus = false
                                        }
                                    )
                                }

                            }
                        }

                        // Select Time
                        ExposedDropdownMenuBox(
                            modifier = Modifier.weight(2f),
                            expanded = expandedTime,
                            onExpandedChange = { expandedTime = !expandedTime }
                        ) {
                            TextField(
                                modifier = Modifier.menuAnchor(),
                                value = timeOptions[selectedTimeFilter] ?: "Tất cả",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Lọc theo thời gian") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedTime,
                                onDismissRequest = { expandedTime = false }
                            ) {
                                timeOptions.forEach { (key, value) ->
                                    DropdownMenuItem(
                                        text = { Text(value) },
                                        onClick = {
                                            selectedTimeFilter = key
                                            expandedTime = false
                                        }
                                    )
                                }
                            }
                        }

                        // Button Filter
                    }

                    Button(
                        onClick = {
                            orderManageViewModel.filterOrders(
                                selectedStatus,
                                selectedTimeFilter
                            )
                            currentPage = 1
                        }
                    ) {
                        Text("Lọc")
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        Crossfade(targetState = isLoading, label = "") { loading ->
                            if (loading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {

                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF8F8F8)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    item {
                                        LazyColumn(
                                            modifier = Modifier
                                                .width(850.dp)
                                                .heightIn(
                                                    max = 1000.dp
                                                )
                                        ) {
                                            item {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color(0xFFE3F2FD))
                                                        .padding(vertical = 8.dp)
                                                ) {
                                                    TableCellHeader(
                                                        "MãOrder",
                                                        Modifier.weight(1f)
                                                    )
                                                    TableCellHeader(
                                                        "Khách hàng",
                                                        Modifier.weight(1.5f)
                                                    )
                                                    TableCellHeader(
                                                        "Tổng tiền",
                                                        Modifier.weight(1.5f)
                                                    )
                                                    TableCellHeader(
                                                        "Ngày tạo đơn hàng",
                                                        Modifier.weight(2f)
                                                    )
                                                    TableCellHeader(
                                                        "Trạng thái",
                                                        Modifier.weight(4f)
                                                    )
                                                }
                                                Divider(color = Color.LightGray, thickness = 1.dp)
                                            }

                                            items(orders) { order ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable {
                                                            navController.navigate("orderManage/preview/${order.id}")
                                                        },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    TableCell(
                                                        text = (order.id).toString(),
                                                        Modifier.weight(1f)
                                                    )

                                                    TableCellNameProduct(
                                                        text = order.name!!,
                                                        Modifier.weight(1.5f)
                                                    )

                                                    TableCell(
                                                        text = formatCurrency(order.tongTien.toDouble()),
                                                        Modifier.weight(1.5f)
                                                    )
                                                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                                                    val formattedDate = sdf.format(order.date)
                                                    TableCell(
                                                        text = formattedDate,
                                                        Modifier.weight(2f)
                                                    )
                                                    Spacer(Modifier.width(50.dp))


                                                    val nextStatus = when (order.status) {
                                                        0 -> 1
                                                        1 -> 2
                                                        2 -> 3
                                                        3 -> 4
                                                        else -> order.status
                                                    }

                                                    val transition = updateTransition(
                                                        targetState = nextStatus,
                                                        label = "StatusTransition"
                                                    )

                                                    val backgroundColor by transition.animateColor(
                                                        label = "ColorAnimation"
                                                    ) { targetStatus ->
                                                        when (targetStatus) {
                                                            0 -> Color(0xFFB0BEC5)
                                                            1 -> Color(0xFF42A5F5)
                                                            2 -> Color(0xFFFFA726)
                                                            3 -> Color(0xFFFB8C00)
                                                            4 -> Color(0xFF66BB6A)
                                                            -1 -> Color(0xFFE53935)
                                                            -2 -> Color(0xFFEF5350)
                                                            else -> Color(0xFFD32F2F)
                                                        }
                                                    }

                                                    val text = when (order.status) {
                                                        0 -> "Đang chờ duyệt"
                                                        1 -> "Đang lấy hàng"
                                                        2 -> "Đang chờ giao hàng"
                                                        3 -> "Đang giao hàng"
                                                        4 -> "Đã giao"
                                                        -1 -> "Nhân viên đã hủy"
                                                        -2 -> "Xem lý do"
                                                        else -> "Khách đã hủy"
                                                    }


                                                    Row(
                                                        modifier = Modifier.weight(4f),
                                                    ) {
                                                        Button(
                                                            onClick = {
                                                                if (order.status != -2 && order.status != 4 && order.status != -1 && order.status != -3) {
                                                                    orderManageViewModel.updateOrderStatus(
                                                                        order.id,
                                                                        nextStatus
                                                                    )
                                                                } else if (order.status == -2) {
                                                                    orderManageViewModel.isWatchReason.value =
                                                                        true
                                                                    orderManageViewModel.reason.value =
                                                                        order.feeback ?: ""
                                                                }
                                                            },
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = backgroundColor
                                                            )
                                                        ) {
                                                            Text(text = text, color = Color.White)
                                                        }

                                                        if (!(order.status == -1 || order.status == -3 || order.status == 4)) {
                                                            Button(
                                                                onClick = {
                                                                    if (order.status != -2) {
                                                                        orderManageViewModel.idOrderCancel =
                                                                            order.id
                                                                        orderManageViewModel.statusOrderCancel =
                                                                            -1
                                                                    } else {
                                                                        orderManageViewModel.idOrderCancel =
                                                                            order.id
                                                                        orderManageViewModel.statusOrderCancel =
                                                                            -3
                                                                    }
                                                                    orderManageViewModel.isCancel.value =
                                                                        true
                                                                },
                                                                colors = ButtonDefaults.buttonColors(
                                                                    containerColor = Color.Red
                                                                )
                                                            ) {
                                                                if (order.status == -2) {
                                                                    Text(
                                                                        text = "Xác nhận đơn hủy",
                                                                        color = Color.White
                                                                    )
                                                                } else {
                                                                    Text(
                                                                        text = "Hủy đơn hàng",
                                                                        color = Color.White,
                                                                        maxLines = 1,
                                                                        overflow = TextOverflow.Ellipsis
                                                                    )
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                                            }
                                        }

                                    }
                                }

                            }
                        }
                    }
                }

            }
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Pagination(
                    totalPages = totalPages,
                    currentPage = currentPage,
                    onPageSelected = { onPageChange.value(it) }, modifier = Modifier.fillMaxWidth()
                )
            }
        }


        SuccessDialog(
            orderManageViewModel.isWatchReason,
            title = "Xem lý do !!!",
            orderManageViewModel.reason.value,
        ) {
            orderManageViewModel.isWatchReason.value = false
            orderManageViewModel.reason.value = ""
        }

        IdentifyDialog(
            openDialog = orderManageViewModel.isCancel,
            "Xác nhận hủy!!",
            "Xác nhận hủy đơn hàng " + orderManageViewModel.idOrderCancel,
            "Confirm"
        ) {
            orderManageViewModel.updateOrderStatus(
                orderManageViewModel.idOrderCancel,
                orderManageViewModel.statusOrderCancel
            )
            orderManageViewModel.isCancel.value = false
            orderManageViewModel.idOrderCancel = 0
            orderManageViewModel.statusOrderCancel = 0

        }

        SuccessDialog(
            orderManageViewModel.isSuccess,
            title = "Success !!!",
            description = "Cập nhật trạng thái đơn hàng !"
        ) {
            orderManageViewModel.isSuccess.value = false
        }

    }

}

@Composable
fun HeaderSectionOrder() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Danh sách đơn hàng",
            fontSize = 22.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight.ExtraBold
        )
    }
}