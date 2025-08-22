package com.example.quanlybandienthoai.view.screens.admin.sanpham

import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.IdentifyDialog
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementScreen(
    navController: NavController,
    productViewModel: ProductManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val pageSize = 7
    var currentPage by remember { mutableStateOf(1) }
    val allProducts by productViewModel.products.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var maSPDelete by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        if (allProducts.isEmpty()) {
            isLoading = true
            productViewModel.getProducts()
            isLoading = false
        }

        mainViewModel.updateSelectedScreen(navController)

    }

    // Chia trang từ list tổng
    val products = remember(currentPage, allProducts) {
        val start = (currentPage - 1) * pageSize
        val end = (start + pageSize).coerceAtMost(allProducts.size)
        if (start in 0 until end) {
            allProducts.subList(start, end)
        } else {
            emptyList()
        }
    }

    val totalPages = (allProducts.size + pageSize - 1) / pageSize


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
            SearchTopBar(navController, productViewModel) { ten ->
                productViewModel.searchByName(ten)
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
                HeaderSectionProduct{
                    navController.navigate(Screen.ManageProduct.AddProduct.route)
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
                                        .background(Color(0xFFF8F8F8))
                                ) {
                                    item {
                                        LazyColumn(
                                            modifier = Modifier
                                                .width(800.dp)
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
                                                    TableCellHeader("MãSP", Modifier.weight(0.5f))
                                                    TableCellHeader("Hình ảnh", Modifier.weight(1f))
                                                    TableCellHeader(
                                                        "Tên sản phẩm",
                                                        Modifier.weight(2f)
                                                    )
                                                    TableCellHeader("Số lượng", Modifier.weight(1f))
                                                    TableCellHeader(
                                                        "Nhập hàng",
                                                        Modifier.weight(1f)
                                                    )
                                                    TableCellHeader(
                                                        "Hành động",
                                                        Modifier.weight(1f)
                                                    )
                                                }
                                                Divider(color = Color.LightGray, thickness = 1.dp)
                                            }

                                            items(products) { product ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable {
                                                            navController.navigate("productManage/preview/${product.masp}")
                                                        }
                                                ) {
                                                    TableCell(
                                                        text = (product.masp).toString(),
                                                        Modifier.weight(0.5f)
                                                    )
                                                    Spacer(Modifier.width(20.dp))
                                                    val imageArray =
                                                        product.hinhanh.split(",").map { it.trim() }

                                                    AsyncImage(
                                                        model = imageArray[0],
                                                        contentDescription = "Product Image",
                                                        modifier = Modifier
                                                            .size(70.dp)
                                                            .border(
                                                                1.dp,
                                                                Color.LightGray,
                                                                RoundedCornerShape(8.dp)
                                                            ),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                    Spacer(Modifier.width(40.dp))

                                                    TableCellNameProduct(
                                                        text = product.tensp,
                                                        Modifier.weight(2f)
                                                    )
                                                    TableCell(
                                                        text = (product.soluongnhap - product.soluongban).toString(),
                                                        Modifier.weight(1f)
                                                    )
                                                    Spacer(Modifier.width(50.dp))


                                                    Row(
                                                        modifier = Modifier.weight(1f),
                                                    ) {
                                                        Button(
                                                            onClick = {
                                                                navController.navigate("productManage/import/${product.masp}")
                                                            },
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = Color(
                                                                    0xFFD81B60
                                                                )
                                                            )
                                                        ) {
                                                            Icon(
                                                                Icons.Default.ShoppingCart,
                                                                contentDescription = "Nhập hàng",
                                                                tint = Color.White
                                                            )
                                                        }
                                                    }

                                                    Row(
                                                        modifier = Modifier.weight(1f),
                                                    ) {
                                                        IconButton(
                                                            onClick = {
                                                                navController.navigate("productManage/edit/${product.masp}")
                                                            },
                                                            modifier = Modifier.background(
                                                                Color(0xFF42A5F5),
                                                                RoundedCornerShape(8.dp)
                                                            )
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Edit,
                                                                contentDescription = "Chỉnh sửa",
                                                                tint = Color.White
                                                            )
                                                        }
                                                        Spacer(Modifier.width(5.dp))

                                                        IconButton(
                                                            onClick = {
                                                                maSPDelete = product.masp
                                                                productViewModel.isConfirm.value =
                                                                    true
                                                            },
                                                            modifier = Modifier.background(
                                                                Color(0xFFE53935),
                                                                RoundedCornerShape(8.dp)
                                                            )
                                                        ) {
                                                            Icon(
                                                                Icons.Default.Delete,
                                                                contentDescription = "Xóa",
                                                                tint = Color.White
                                                            )
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


        IdentifyDialog(
            openDialog = productViewModel.isConfirm,
            "Xóa sản phẩm !!",
            "Bạn có muốn xóa sản phẩm này không ?",
            "Confirm"
        ) {
            productViewModel.deleteProduct(
                productId = maSPDelete
            )
            productViewModel.isConfirm.value = false
            maSPDelete = 0

        }

        SuccessDialog(
            openDialog = productViewModel.isImport,
            "Success !!!",
            "Nhập sản phẩm thành công !",
            "Xác nhận"
        ) {
            productViewModel.isImport.value = false
            productViewModel.getProducts()
        }

        SuccessDialog(
            openDialog = productViewModel.isUpsert,
            "Success !!!",
            "Lưu sản phẩm thành công !",
            "Xác nhận"
        ) {
            productViewModel.isUpsert.value = false
            productViewModel.getProducts()
        }

    }

}

@Composable
fun HeaderSectionProduct(onAddClick:()->Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Danh sách sản phẩm",
            fontSize = 22.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight.ExtraBold
        )
        Row {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Thêm mới", color = Color.White)
            }
        }
    }
}

@Composable
fun TableCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .padding(8.dp),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
    )
}

@Composable
fun TableCellNameProduct(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .padding(8.dp)
            .width(20.dp),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
    )
}


@Composable
fun TableCellHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier.padding(8.dp),
        textAlign = TextAlign.Center,
        fontFamily = fontFamily(),
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
fun Pagination(
    totalPages: Int,
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxVisiblePages = 5

    fun generatePageNumbers(): List<Int?> {
        val pages = mutableListOf<Int?>()

        if (totalPages <= maxVisiblePages) {
            (1..totalPages).forEach { pages.add(it) }
        } else {
            if (currentPage <= 3) {
                (1..3).forEach { pages.add(it) }
                pages.add(null) // dấu ...
                pages.add(totalPages)
            } else if (currentPage >= totalPages - 2) {
                pages.add(1)
                pages.add(null) // dấu ...
                ((totalPages - 2)..totalPages).forEach { pages.add(it) }
            } else {
                pages.add(1)
                pages.add(null) // dấu ...
                (currentPage - 1..currentPage + 1).forEach { pages.add(it) }
                pages.add(null) // dấu ...
                pages.add(totalPages)
            }
        }

        return pages
    }

    val pages = generatePageNumbers()

    LazyRow(
        modifier = modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        item {
            if (currentPage > 1) {
                Button(
                    onClick = { onPageSelected(currentPage - 1) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text("<")
                }
            }
        }

        items(pages) { page ->
            if (page != null) {
                Button(
                    onClick = { onPageSelected(page) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (page == currentPage) Color(0xFF4CAF50) else Color(
                            0xFF2196F3
                        )
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        page.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Dấu "..." không phải button
                Text(
                    "...",
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }



        item {
            if (currentPage < totalPages) {
                Button(
                    onClick = { onPageSelected(currentPage + 1) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(">")
                }
            }
        }
    }
}

