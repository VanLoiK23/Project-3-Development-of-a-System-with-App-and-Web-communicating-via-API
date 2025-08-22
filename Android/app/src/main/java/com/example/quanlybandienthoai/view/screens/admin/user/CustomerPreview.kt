package com.example.quanlybandienthoai.view.screens.admin.user

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.CustomerManageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongTinKhachHangScreen(
    navController: NavController,
    id: Int,
    customerManageViewModel: CustomerManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    LaunchedEffect(id) {
        mainViewModel.updateSelectedScreen(navController)
        customerManageViewModel.setCurrentCustomer(id)
    }

    val khachhang = customerManageViewModel.customer


    if(khachhang!=null){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                "Thông tin khách hàng " + khachhang.firstName,
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
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ảnh đại diện theo giới tính
                val avatar =
                    if (khachhang.gender == "male") R.drawable.avatar_male else R.drawable.avatar_female

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = avatar),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.width(50.dp))

                    Column(modifier = Modifier.weight(2f)) {
                        ThongTinTieuDe("Họ tên", "${khachhang.firstName} ${khachhang.lastName}")
                        ThongTinTieuDe("Số điện thoại", khachhang.phone)
                        ThongTinTieuDe("Email", khachhang.email)
                        ThongTinTieuDe("Ngày sinh", khachhang.ngaysinh)
                        ThongTinTieuDe("Giới tính", if (khachhang.gender == "male") "Nam" else "Nữ")
                    }
                }

            }
        }
    }
}

@Composable
fun ThongTinTieuDe(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

