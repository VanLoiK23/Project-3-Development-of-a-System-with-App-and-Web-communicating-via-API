package com.example.quanlybandienthoai.view.screens.admin.sanpham

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    navController: NavController,
    id: Int,
    productViewModel: ProductManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    LaunchedEffect(id) {
        mainViewModel.updateSelectedScreen(navController)
        productViewModel.setCurrentProduct(id)
    }

    val product by productViewModel.product.collectAsState()
    val brand by productViewModel.brand.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Chi tiết sản phẩm " + product.tensp,
                            fontSize = 15.sp,
                            fontFamily = fontFamily(),
                            fontWeight = FontWeight.Medium
                        )
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
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)) // Màu nền nhẹ
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF00C2FF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Chi tiết thông tin sản phẩm",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                val imageArray =
                    product.hinhanh.split(",").map { it.trim() }

                AsyncImage(
                    model = imageArray[0],
                    contentDescription = "Hình ảnh sản phẩm",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(2f)
                ) {
                    ProductInfoItem(label = "Tên sản phẩm", value = product.tensp)
                    ProductInfoItem(label = "Xuất xứ", value = product.xuatxu)
                    ProductInfoItem(
                        label = "Dung lượng pin",
                        value = product.dungluongpin.toString() + " mAh"
                    )
                    ProductInfoItem(label = "Màn hình", value = product.manhinh)
                    ProductInfoItem(
                        label = "Camera sau",
                        value = product.camerasau
                    )

                    ProductInfoItem(label = "Camera trước", value = product.cameratruoc)
                    ProductInfoItem(label = "Hệ điều hành", value = product.hedieuhanh)
                    ProductInfoItem(label = "Thương hiệu", value = brand?.tenthuonghieu)
                    ProductInfoItem(label = "Phiên bản HĐH", value = product.phienbanhdh.toString())
                    ProductInfoItem(label = "Khu vực kho", value = product.khuvuckho)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProductQuantity(
                    label = "Số lượng nhập",
                    value = product.soluongnhap.toString(),
                    modifier = Modifier.weight(1f)
                )
                ProductQuantity(
                    label = "Số lượng bán",
                    value = product.soluongban.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            VariantTable(variantList = product.pbspList)
        }
    }
}


@Composable
fun ProductInfoItem(label: String, value: String?) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF888888),
            fontFamily = fontFamily(),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFE0E0E0)
        ) {
            Text(
                text = value ?: "Không",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun ProductQuantity(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF7D32A8),
            fontFamily = fontFamily(),
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFE0E0E0)
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun VariantTable(variantList: List<Phienbansanpham>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp)) // Nền nhạt sang trọng
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Danh sách phiên bản sản phẩm",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A),
            fontFamily = fontFamily(),
            modifier = Modifier.padding(bottom = 16.dp)
        )


        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3B82F6), RoundedCornerShape(12.dp))
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TableHeaderItem(text = "STT", modifier = Modifier.weight(1f))
            TableHeaderItem(text = "Ram", modifier = Modifier.weight(2f))
            TableHeaderItem(text = "Rom", modifier = Modifier.weight(2f))
            TableHeaderItem(text = "Màu sắc", modifier = Modifier.weight(2f))
            TableHeaderItem(text = "Giá nhập", modifier = Modifier.weight(2f))
            TableHeaderItem(text = "Giá bán", modifier = Modifier.weight(2f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Data
        variantList.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (index % 2 == 0) Color(0xFFFFFFFF) else Color(0xFFE0F2FE), // trắng & xanh siêu nhạt
                        RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TableCellItem(text = "${index + 1}", Modifier.weight(1f))
                TableCellItem(
                    text = if (item.ram != null) {
                        item.ram+" GB"
                    } else {
                        item.ram
                    }, Modifier.weight(2f)
                )
                TableCellItem(text =  if (item.rom != null) {
                    item.rom+" GB"
                } else {
                    item.rom
                }, Modifier.weight(2f))
                TableCellItem(text = item.mausac, Modifier.weight(2f))
                TableCellItem(text = formatCurrency(item.gianhap), Modifier.weight(2f))
                TableCellItem(text = formatCurrency(item.price_sale), Modifier.weight(2f))
            }
        }
    }
}

@Composable
fun TableHeaderItem(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = Color(0xFF003366),
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TableCellItem(text: String?, modifier: Modifier = Modifier) {
    Text(
        text = text ?: "Không",
        fontSize = 9.sp,
        color = Color.Black,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}


