package com.example.quanlybandienthoai.view.screens.profile.address

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.model.remote.entity.address
import com.example.quanlybandienthoai.viewmodel.AddreesViewModel
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextButton
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.view.components.AnimatedLoadingIndicator
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navController: NavController,
    context: Application = LocalContext.current.applicationContext as Application,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    addreesViewModel: AddreesViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
) {

    LaunchedEffect(addreesViewModel.isUpsert) {
//        if (appViewModel.getCurrentUser() != null) {
        addreesViewModel.idkh.value = appViewModel.getCurrentUserId().toInt()
        if (addreesViewModel.addresss.isEmpty() || addreesViewModel.isUpsert) {
            addreesViewModel.getAllAddress()
            //}
        }

        if (!addreesViewModel.isSelected.value) {
            mainViewModel.updateSelectedScreen(navController)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        androidx.compose.material3.Text("List Address")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF7D32A8),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Account.InsertAddress.route)
                },
//                        containerColor = Color.Gray,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        },
        bottomBar = {
            if (!addreesViewModel.isSelected.value) {
                BottomNavigationBar(navController, mainViewModel)
            }
        }
    ) { innerPadding ->

        if (addreesViewModel.addresss.isEmpty() && !addreesViewModel.isLoading) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.address),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material3.Text(
                    "Địa chỉ giao hàng chưa có",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                androidx.compose.material3.Text(
                    "Bạn chưa thêm địa chỉ giao hàng nào !",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else {
            if (addreesViewModel.isLoading) {
                AnimatedLoadingIndicator(addreesViewModel.isLoading)
            } else {
                var showDialog by remember { mutableStateOf(false) }
                var idAddress by remember { mutableStateOf(-1) }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(addreesViewModel.addresss) { address ->
                        AddressCard(address = address, eventClick = { id ->
                            val detailScreen = Screen.Account.AddressDetail(id)
                            addreesViewModel.getAddress(id)

                            if (addreesViewModel.isSelected.value) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("account/address/detail/$id")
                            }
                        }, onDeleteClick = { id ->
                            showDialog = true
                            idAddress = id
                        })
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Xác nhận xóa") },
                        text = { Text("Bạn có chắc muốn xóa địa chỉ này không?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    addreesViewModel.deleteAddress(idAddress)
                                    showDialog = false
                                }
                            ) {
                                Text("Xóa", color = Color.Red)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Hủy")
                            }
                        }
                    )
                }
                SuccessDialog(
                    openDialog = addreesViewModel.isSuccess,
                    "Success !!",
                    "Delete Address Complete",
                    "Confirm"
                ) {
                    addreesViewModel.isSuccess.value = false
                    navController.navigate(Screen.Account.Address.route)
                }
            }
        }
    }
}

@Composable
fun AddressCard(
    address: address,
    modifier: Modifier = Modifier,
    eventClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { eventClick(address.id) },
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp,
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color(0xFF7D32A8), CircleShape)
                        .shadow(6.dp, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(R.drawable.address_img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = address.hovaten,
                        fontSize = 20.sp,
                        color = Color(0xFF7D32A8),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Đường ${address.street_name}, Quận ${address.district}, TP ${address.city}, ${address.country}",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "SĐT: ${address.sodienthoai} | Email: ${address.email}",
                        fontSize = 15.sp,
                        color = Color(0xFF1976D2),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (address.note.isNotEmpty()) {
                        Text(
                            text = "Ghi chú: ${address.note}",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Nút xoá đẹp - nổi, đỏ, có viền và shadow
        IconButton(
            onClick = { onDeleteClick(address.id) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 10.dp, y = (-10).dp)
                .background(Color.Red, shape = CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .shadow(6.dp, shape = CircleShape)
                .size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Xoá",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


