package com.example.quanlybandienthoai.view.screens.admin.user

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
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
import com.example.quanlybandienthoai.viewmodel.admin.CustomerManageViewModel
import com.example.quanlybandienthoai.viewmodel.admin.OrderManageViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    navController: NavController,
    customerManageViewModel: CustomerManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val pageSize = 7
    var currentPage by remember { mutableStateOf(1) }
    val allCustomers by customerManageViewModel.customers.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var maKHIsSelected by remember { mutableStateOf(0) }
    var statusCofirm by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (allCustomers.isEmpty()) {
            isLoading = true
            customerManageViewModel.getCustomers()
            isLoading = false
        }

        mainViewModel.updateSelectedScreen(navController)

    }

    // Chia trang từ list tổng
    val customers = remember(currentPage, allCustomers) {
        val start = (currentPage - 1) * pageSize
        val end = (start + pageSize).coerceAtMost(allCustomers.size)
        if (start in 0 until end) {
            allCustomers.subList(start, end)
        } else {
            emptyList()
        }
    }

    val totalPages = (allCustomers.size + pageSize - 1) / pageSize


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
           SearchTopBar(
                navController,
               customerManageViewModel
            ) { ten ->
                customerManageViewModel.searchByName(ten)
                currentPage = 1
            }
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
                                                        "MãKH",
                                                        Modifier.weight(1f)
                                                    )
                                                    TableCellHeader(
                                                        "Họ tên",
                                                        Modifier.weight(1.5f)
                                                    )
                                                    TableCellHeader(
                                                        "Email",
                                                        Modifier.weight(2f)
                                                    )
                                                    TableCellHeader(
                                                        "Số điện thoại",
                                                        Modifier.weight(1.5f)
                                                    )
                                                    TableCellHeader(
                                                        "Hành động",
                                                        Modifier.weight(3f)
                                                    )
                                                }
                                                Divider(color = Color.LightGray, thickness = 1.dp)
                                            }

                                            items(customers) { ct ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable {
                                                            navController.navigate("userManage/preview/${ct.id}")
                                                        },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    TableCell(
                                                        text = (ct.id).toString(),
                                                        Modifier.weight(1f)
                                                    )

                                                    TableCellNameProduct(
                                                        text = ct.lastName + " " + ct.firstName,
                                                        Modifier.weight(1.5f)
                                                    )

                                                    TableCell(
                                                        text = ct.email,
                                                        Modifier.weight(2f)
                                                    )

                                                    TableCell(
                                                        text = ct.phone,
                                                        Modifier.weight(1.5f)
                                                    )



                                                    val nextStatus = when (ct.status) {
                                                        "active" -> "lock"
                                                        else -> "active"
                                                    }

                                                    val transition = updateTransition(
                                                        targetState = nextStatus,
                                                        label = "StatusTransition"
                                                    )

                                                    val backgroundColor by transition.animateColor(
                                                        label = "ColorAnimation"
                                                    ) { targetStatus ->
                                                        when (targetStatus) {
                                                            "active" -> Color(0xFFFB8C00)
                                                            "lock" -> Color(0xFF66BB6A)
//                                                            -1 -> Color(0xFFE53935)
//                                                            -2 -> Color(0xFFEF5350)
                                                            else -> Color(0xFFD32F2F)
                                                        }
                                                    }

                                                    val icon = when (ct.status) {
                                                        "active" -> Icons.Default.Lock
                                                        else -> Icons.Default.LockOpen
                                                    }

                                                    Row(
                                                        modifier = Modifier.weight(3f),
                                                    ) {
                                                        Spacer(Modifier.width(80.dp))

                                                        Button(
                                                            onClick = {
                                                                statusCofirm = ct.status
                                                                maKHIsSelected = ct.id
                                                                customerManageViewModel.isComfirm.value =
                                                                    true
                                                            },
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = backgroundColor
                                                            )
                                                        ) {
                                                            Icon(
                                                                icon,
                                                                contentDescription = "Status",
                                                                tint = Color.White
                                                            )
                                                        }

                                                        Button(
                                                            onClick = {
                                                                navController.navigate("userManage/edit/${ct.id}")
                                                            },
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFF42A5F5)
                                                            )
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Edit,
                                                                contentDescription = "Chỉnh sửa",
                                                                tint = Color.White
                                                            )
                                                        }


                                                        Button(
                                                            onClick = {
                                                                maKHIsSelected = ct.id
                                                                customerManageViewModel.isComfirmDelete.value =
                                                                    true
                                                            },
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFFEF5350)
                                                            )
                                                        ) {
                                                            Icon(
                                                                Icons.Default.Delete,
                                                                contentDescription = "Xóa",
                                                                tint = Color.White
                                                            )
                                                        }
//
//                                                        IconButton(
//                                                            onClick = {
//                                                                maKHIsSelected = ct.id
//                                                                customerManageViewModel.isComfirmDelete.value =
//                                                                    true
//                                                            },
//                                                            modifier = Modifier.background(
//                                                                Color(0xFFE53935),
//                                                                RoundedCornerShape(8.dp)
//                                                            )
//                                                        ) {
//                                                            Icon(
//                                                                Icons.Default.Delete,
//                                                                contentDescription = "Xóa",
//                                                                tint = Color.White
//                                                            )
//                                                        }


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

        IdentifyDialog(
            openDialog = customerManageViewModel.isComfirmDelete,
            "Xóa tài khoản !!",
            "Bạn có muốn xóa tài khoản này không ?",
            "Confirm"
        ) {
            customerManageViewModel.updateCustomerStatus(
                maKHIsSelected,
                "deleted"
            )
            customerManageViewModel.isComfirmDelete.value = false
            maKHIsSelected = 0
        }

        IdentifyDialog(
            openDialog = customerManageViewModel.isComfirm,
            if (statusCofirm == "active") {
                "Khóa tài khoản !!"
            } else {
                "Mở khóa tài khoản"
            },
            "Bạn có muốn " +
                    if (statusCofirm == "active") {
                        "khóa"
                    } else {
                        "mở khóa"
                    } +
                    " tài khoản này không ?",
            "Confirm"
        ) {

            customerManageViewModel.updateCustomerStatus(
                maKHIsSelected,
                if (statusCofirm == "active") {
                    "lock"
                } else {
                    "active"
                }
            )

            customerManageViewModel.isComfirm.value = false
            maKHIsSelected = 0
        }

        SuccessDialog(
            openDialog = customerManageViewModel.isEdit,
            "Success !!!",
            "Lưu TT khách hàng thành công !",
            "Xác nhận"
        ) {
            customerManageViewModel.isEdit.value = false
            customerManageViewModel.getCustomers()
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
            text = "Danh sách khách hàng",
            fontSize = 22.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight.ExtraBold
        )
    }
}