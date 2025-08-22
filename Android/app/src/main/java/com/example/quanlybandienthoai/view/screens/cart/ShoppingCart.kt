package com.example.quanlybandienthoai.view.screens.cart

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.view.components.AnimatedLoadingIndicator
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.TopBar
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.worker.SyncFirebaseToApiWorker

//@ExperimentalMaterial3Api
//@Composable
//fun ShoppingCart(
//    navController: NavHostController,
//    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
//    viewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
//    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
//) {
//    viewModel.setUser(
//        viewModel<AppViewModel>(LocalContext.current as ComponentActivity).getCurrentUser(),
//    )
//
//    viewModel.setUser(User("1", "", "", "", "", "", false, ""))
////    viewModel.insertCart(Cart(-1, appViewModel.getCurrentUserId().toInt()), CartItem(-1, -1, 33, 3))
//
//    Scaffold(topBar = {
//        TopBar("Giỏ hàng", false, navBack = {
//            navController.popBackStack()
//            mainViewModel.updateSelectedScreen(navController)
//        })
//    },
//        content = { padding ->
//            Column(
//                Modifier
//                    .padding(padding)
//                    .padding(20.dp)
//                    .fillMaxHeight()
//            ) {
//                val userCart = viewModel.userCart.value
//
//                if (userCart.isEmpty()) {
//                    Column(
//                        Modifier
//                            .fillMaxWidth()
//                            .fillMaxHeight(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.empty_cart),
//                            contentDescription = "Empty cart image",
//                            Modifier.size(250.dp)
//                        )
//                        Text("Giỏ hàng đang trống", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                        Text(
//                            "Bạn chưa thêm bất kì gì vào giỏ hàng!",
//                            Modifier.padding(bottom = 10.dp)
//                        )
//                        Button(onClick = {
//                            navController.navigate(Screen.Home.route)
//
//                            mainViewModel.updateSelectedScreen(navController)
//                        }) {
//                            Text("Mua sắm")
//                        }
//                    }
//                } else {
//                    LazyColumn(Modifier.weight(4f)) {
//                        items(viewModel.cartItem.value.size) {
//
//                            var i = 0
//                            userCart.forEach { (sanpham, phienbansanphams) ->
//                                phienbansanphams.forEach { pb ->
//                                    CartItemCard(
//                                        sanpham, pb, viewModel.cartItem.value[i], viewModel
//                                    ) {
//                                        val detailScreen = Screen.ProductDetail(sanpham.masp)
//                                        mainViewModel.selectScreen(detailScreen)
//                                        navController.navigate("productDetail/${sanpham.masp}")
//                                    }
//                                    i++
//                                }
//                            }
//
//                        }
//                    }
//
//                    Divider(Modifier.padding(top = 10.dp))
//
//                    Column(
//                        Modifier.padding(start = 10.dp, end = 10.dp)
//                    ) {
//                        val totalPrice = viewModel.totalPrice.value
//                        Row(
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(top = 10.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text("Total:", fontSize = 16.sp)
//                            Text(
//                                formatCurrency(totalPrice),
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            )
//                        }
//                        Row(
//                            Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text("Vận chuyển:", fontSize = 16.sp)
//                            Text(
//                                "${formatCurrency(viewModel.totalTransport.value)}%",
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            )
//                        }
//                        Row(
//                            Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text("Thuế:", fontSize = 16.sp)
//                            Text(
//                                "${formatCurrency(viewModel.totalTax.value.toDouble())}%",
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            )
//                        }
//                        Row(
//                            Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 10.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text("Tổng cộng:", fontSize = 16.sp)
//                            Text(
//                                formatCurrency(viewModel.grandPrice.value),
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            )
//                        }
//
//                        Button(
//                            onClick = {
////                                navController.navigate(Screen.SelectAddress.route)
//                            },
//                            Modifier.fillMaxWidth(),
//                        ) {
//                            Text(text = "CHECKOUT", fontSize = 18.sp)
//                        }
//                    }
//                }
//            }
//        },
//        bottomBar = {
//            BottomNavigationBar(navController, mainViewModel)
//        })
//}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCart(
    navController: NavHostController,
    context: Application = LocalContext.current.applicationContext as Application,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    viewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    //viewModel.setUser(appViewModel.getCurrentUser())
//    viewModel.insertCart(Cart(-1, 1), CartItem(-1, -1, 33,1, 3))

//    LaunchedEffect(Unit) {
//        viewModel.setUser(appViewModel.getCurrentUser())
//    }

    val openRemoveCartItemDialog = remember { mutableStateOf(false) }

    val userId = appViewModel.getCurrentUserId()

//    LaunchedEffect(userId) {
//        val inputData = workDataOf("userId" to userId)
//
//        val workRequest = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
//            .setInputData(inputData)
//            .build()
//
//        WorkMan ager.getInstance(context).enqueue(workRequest)
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("🛒 Giỏ hàng", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()

                        mainViewModel.updateSelectedScreen(navController)
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (viewModel.userCart.value.isNotEmpty()) {
                            openRemoveCartItemDialog.value = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Clear Cart",
                            tint = Color.White
                        )

                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )

        },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                val userCart = viewModel.userCart.collectAsState().value
                val cartItemList = viewModel.cartItem.collectAsState().value

                LaunchedEffect(userCart, cartItemList) {
                    val inputData = workDataOf("userId" to userId)

                    val workRequest = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
                        .setInputData(inputData)
                        .build()

                    WorkManager.getInstance(context).enqueue(workRequest)
                }


                if (userCart.isEmpty() && !viewModel.isLoading) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.empty_cart),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Giỏ hàng trống", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Bạn chưa thêm sản phẩm nào vào giỏ hàng!",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                navController.navigate(Screen.Home.route)
                                mainViewModel.updateSelectedScreen(navController)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("🛍️ Mua sắm ngay", fontSize = 16.sp)
                        }
                    }
                } else {

                    if (viewModel.isLoading) {
                        AnimatedLoadingIndicator(viewModel.isLoading)
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                val flatList = userCart.flatMap { (sanpham, pbList) ->
                                    pbList.map { pb -> sanpham to pb }
                                }

                                flatList.zip(cartItemList).forEach { (pair, cartItem) ->
                                    val (sanpham, pb) = pair
                                    CartItemCard(sanpham, pb, cartItem, viewModel) {
                                        val detailScreen = Screen.ProductDetail(sanpham.masp)
                                        mainViewModel.selectScreen(detailScreen)
                                        navController.navigate("productDetail/${sanpham.masp}")
                                    }
                                }

                            }

                        }
                        Divider(Modifier.padding(top = 10.dp))
                        Column(Modifier.padding(16.dp)) {
                            val totalPrice = viewModel.totalPrice.collectAsState().value
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tổng tiền:", fontSize = 18.sp)
                                Text(
                                    formatCurrency(totalPrice),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            val totalTransport = viewModel.totalTransport.collectAsState().value

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Vận chuyển:", fontSize = 16.sp)
                                Text(
                                    formatCurrency(totalTransport),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            val totalTax = viewModel.totalTax.collectAsState().value

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Thuế:", fontSize = 16.sp)
                                Text(
                                    totalTax.toString() + "%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            val grandPrice = viewModel.grandPrice.collectAsState().value

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tổng cộng:", fontSize = 16.sp)
                                Text(
                                    formatCurrency(grandPrice),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Red
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    viewModel.checkOut { message ->
                                        if (message == null) {
                                            navController.navigate(Screen.Checkout.route)
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("💳 Checkout ngay", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            if (viewModel.isWarning.value) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.isWarning.value = false
                        viewModel.messWarning = ""
                    },
                    title = {
                        Text("Cảnh báo !!!!!")
                    },
                    text = {
                        Text(text = viewModel.messWarning)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.isWarning.value = false
                                viewModel.messWarning = ""
                            }
                        ) {
                            Text("Xác nhận")
                        }

                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                viewModel.isWarning.value = false
                                viewModel.messWarning = ""
                            }) {
                            Text("Cancel")
                        }
                    }
                )
            }


            if (viewModel.message == "Số lượng vượt quá tồn kho") {
                openRemoveCartItemDialog.value = true
            }

            if (openRemoveCartItemDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        openRemoveCartItemDialog.value = false
                        viewModel.message = ""
                    },
                    title = {
                        if (viewModel.message == "Số lượng vượt quá tồn kho") {
                            Text(text = viewModel.message)
                        } else {
                            Text(text = "Xóa giỏ hàng")
                        }
                    },
                    text = {
                        if (viewModel.message == "Số lượng vượt quá tồn kho") {
                            Text(text = viewModel.message)
                        } else {
                            Text("Bạn có muốn xóa tất cả sản phẩm ra khỏi giỏ hàng?")
                        }
                    },
                    confirmButton = {
                        if (viewModel.message == "Số lượng vượt quá tồn kho") {
                            Button(
                                onClick = {
                                    openRemoveCartItemDialog.value = false
                                    viewModel.message = ""
                                }
                            ) {
                                Text("Xác nhận")
                            }
                        } else {
                            Button(
                                onClick = {
                                    openRemoveCartItemDialog.value = false
                                    viewModel.deleteAllCartItem(cart = viewModel.cartItem.value[0].cart_id)
//                                    viewModel.deletedAllCartItem()
                                }
                            ) {
                                Text("Yes")
                            }
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openRemoveCartItemDialog.value = false
                                viewModel.message = ""
                            }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        },
        bottomBar = { BottomNavigationBar(navController, mainViewModel) }
    )
}
