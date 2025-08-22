package com.example.quanlybandienthoai.view.screens.admin.category

import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.IdentifyDialog
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.screens.admin.sanpham.Pagination
import com.example.quanlybandienthoai.view.screens.admin.sanpham.TableCell
import com.example.quanlybandienthoai.view.screens.admin.sanpham.TableCellHeader
import com.example.quanlybandienthoai.view.screens.admin.sanpham.TableCellNameProduct
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.CategoryManageViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    navController: NavController,
    categoryManageViewModel: CategoryManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val pageSize = 7
    var currentPage by remember { mutableStateOf(1) }
    val allBrands by categoryManageViewModel.brands.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if(allBrands.isEmpty()) {
            isLoading = true
            categoryManageViewModel.getBrands()
            isLoading = false
        }

        mainViewModel.updateSelectedScreen(navController)

    }

    // Chia trang từ list tổng
    val brands = remember(currentPage, allBrands) {
        val start = (currentPage - 1) * pageSize
        val end = (start + pageSize).coerceAtMost(allBrands.size)
        if (start in 0 until end) {
            allBrands.subList(start, end)
        } else {
            emptyList()
        }
    }

    val totalPages = (allBrands.size + pageSize - 1) / pageSize


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
                categoryManageViewModel
            ) { ten ->
                categoryManageViewModel.searchByName(ten)

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
                HeaderSectionCategory {
                    navController.navigate("brandManage/upsert/${-1}")
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
                                                .width(500.dp)
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
                                                    TableCellHeader("MãTH", Modifier.weight(1f))
                                                    TableCellHeader("Hình ảnh", Modifier.weight(2f))
                                                    TableCellHeader(
                                                        "Tên thương hiệu",
                                                        Modifier.weight(2f)
                                                    )
                                                }
                                                Divider(color = Color.LightGray, thickness = 1.dp)
                                            }

                                            items(brands) { brand ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable {
                                                            navController.navigate("brandManage/upsert/${brand.mathuonghieu}")
                                                        },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    TableCell(
                                                        text = (brand.mathuonghieu).toString(),
                                                        Modifier.weight(1f)
                                                    )
                                                    Spacer(Modifier.width(50.dp))

                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.Center, // căn giữa theo chiều dọc luôn
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(Color.White)
                                                            .padding(2.dp)
                                                            .weight(2f)
                                                    ) {
                                                        AsyncImage(
                                                            model = brand.image,
                                                            contentDescription = brand.tenthuonghieu,
                                                            modifier = Modifier
                                                                .size(80.dp)
                                                                .clip(RoundedCornerShape(12.dp)),
                                                            contentScale = ContentScale.Fit,
                                                            placeholder = painterResource(id = R.drawable.logo), // hình placeholder nếu chưa load
                                                            error = painterResource(id = R.drawable.logo) // hình nếu lỗi ảnh
                                                        )

                                                    }



                                                    Spacer(Modifier.width(40.dp))

                                                    TableCellNameProduct(
                                                        text = brand.tenthuonghieu,
                                                        Modifier.weight(2f)
                                                    )
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

    }

}

@Composable
fun HeaderSectionCategory(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Danh sách thương hiệu",
            fontSize = 22.sp,
            fontFamily = fontFamily(),
            fontWeight = FontWeight.ExtraBold
        )
        Row {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Thêm mới", color = Color.White)
            }
        }
    }
}
