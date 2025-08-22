package com.example.quanlybandienthoai.view.screens.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.Discount
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.entity.address
import com.example.quanlybandienthoai.model.remote.entity.chitietphieuxuat
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.CartRepository
import com.example.quanlybandienthoai.payment.MomoWebViewActivity
import com.example.quanlybandienthoai.payment.OrderActivity
import com.example.quanlybandienthoai.view.components.AnimatedLoadingIndicator
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.components.TopBar
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.view.screens.cart.CartItemCard
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AddreesViewModel
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.PlaceOrderViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavHostController,
    viewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
    addreesViewModel: AddreesViewModel = viewModel(LocalContext.current as ComponentActivity),
    placeOrderViewModel: PlaceOrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

//    LaunchedEffect(isSuccess) {
//        if (isSuccess) {
//            navController.navigate(Screen.Home.route) {
//                popUpTo(Screen.Checkout.route) { inclusive = true }
//            }
//        }
//    }


    val context = LocalContext.current
//    val placeOrderViewModel: PlaceOrderViewModel = viewModel()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val success = result.data?.getBooleanExtra("payment_success", false) ?: false
            if (success) {
                placeOrderViewModel.markPaymentSuccess()
            }
        }
    }

    val isSuccess = placeOrderViewModel.isPaymentSuccess
    viewModel.getIdCart(appViewModel.getCurrentUserId().toInt())

    val idCart = viewModel.idCart


    Log.d("Test123", "Id cart $idCart")

    if (isSuccess) {
        AlertDialog(
            onDismissRequest = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Checkout.route) { inclusive = true }

                    placeOrderViewModel.isPaymentSuccess = false

                    viewModel.updateComplete(idCart)
                    placeOrderViewModel.setCartCompleted(idCart)
                    viewModel.idCart = -1
                }
            },
            title = { Text("Thanh toán thành công") },
            text = { Text("Đơn hàng của bạn đã được ghi nhận.") },
            confirmButton = {
                TextButton(onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Checkout.route) { inclusive = true }
                    }

                    placeOrderViewModel.isPaymentSuccess = false

                    viewModel.updateComplete(idCart)
                    placeOrderViewModel.setCartCompleted(idCart)
                    viewModel.idCart = -1
                }) {
                    Text("OK")
                }
            }
        )
    }

    Log.d("CHECK_PAYMENT", "isPaymentSuccess = $isSuccess")


    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    var vouncherSelect by remember { mutableStateOf("") }

    val totalPrice by viewModel.totalPrice.collectAsState()

    val selectedDiscount = placeOrderViewModel.selectedDiscount

    val grandPrice by remember(totalPrice, selectedDiscount) {
        derivedStateOf {
            val discounted = totalPrice - (selectedDiscount?.discountAmount ?: 0)
            val fee = if (discounted > 2_000_000) 0.0 else 200_000.0
            discounted + fee + (discounted + fee) / 10
        }
    }

    var messageToShop by remember {
        mutableStateOf(
            if (addreesViewModel.note.value.isEmpty() && addreesViewModel.addresss.isNotEmpty()) {
                addreesViewModel.addresss[0].note
            } else {
                addreesViewModel.note.value
            }
        )
    }
    val messageToShopConst by remember {
        derivedStateOf {
            if (addreesViewModel.note.value.isEmpty() && addreesViewModel.addresss.isNotEmpty()) {
                addreesViewModel.addresss[0].note
            } else {
                addreesViewModel.note.value
            }
        }
    }

    var showMessageSheet by remember { mutableStateOf(false) }
    val messageSheetState = rememberModalBottomSheetState()

    val userCart = viewModel.userCart.collectAsState().value
    val cartItemList = viewModel.cartItem.collectAsState().value
    var selectedPayment by remember { mutableStateOf(PaymentMethod.COD) }




    LaunchedEffect(addreesViewModel.isUpsert) {
//        if (appViewModel.getCurrentUser() != null) {
        addreesViewModel.idkh.value = appViewModel.getCurrentUserId().toInt()
        if (addreesViewModel.addresss.isEmpty() || addreesViewModel.isUpsert) {
            addreesViewModel.getAllAddress()
            //}
        }
        if (placeOrderViewModel.discounts.isEmpty()) {
            placeOrderViewModel.getAllDiscount()
        }
    }



    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White
        ) {
            VoucherPicker(
                onVoucherSelected = {
                    showSheet = false
                    vouncherSelect = it.code
                    placeOrderViewModel.selectedDiscount = it
                }, placeOrderViewModel.discounts, viewModel.grandPrice.collectAsState().value
            )
        }
    }

    if (showMessageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMessageSheet = false },
            sheetState = messageSheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White
        ) {
            MessageInputSheet(initialText = messageToShop, onDone = {
                messageToShop = it
                showMessageSheet = false
            })
        }
    }


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "💳 Thanh toán",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily()
            )
        }, navigationIcon = {
            IconButton(onClick = {
                addreesViewModel.isSelected.value = false

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
        CheckoutBottomBar(total = grandPrice, onOrderClick = {
            // TODO: Xử lý đặt hàng tại đây
//                    Toast.makeText(LocalContext.current, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()

            if (addreesViewModel.addresss.isEmpty()) {

                placeOrderViewModel.isNotValidAddress.value = true
            } else {


                if (messageToShop.isEmpty()) {
                    messageToShop = messageToShopConst
                }

                //neu co thay doi dia chi
                if (addreesViewModel.note.value.isEmpty() && addreesViewModel.addresss.isNotEmpty()) {
                    placeOrderViewModel.cart_shipping = addreesViewModel.addresss[0].id

                    if (messageToShop != addreesViewModel.addresss[0].note) {

                        val add = addreesViewModel.addresss[0]

                        val address = address(
                            -1,
                            addreesViewModel.idkh.value,
                            add.hovaten,
                            add.email,
                            add.sodienthoai,
                            add.street_name,
                            add.district,
                            add.city,
                            "Việt Nam",
                            messageToShop
                        )

                        addreesViewModel.updateExistingAddress(
                            addreesViewModel.addresss[0].id,
                            address
                        )

                        Log.d("Test", "Ok baby")
                    }
                } else {
                    placeOrderViewModel.cart_shipping = addreesViewModel.id_shipping.value

                    if (messageToShop != addreesViewModel.note.value) {

                        val address = address(
                            -1,
                            addreesViewModel.idkh.value,
                            addreesViewModel.hovaten.value,
                            addreesViewModel.email.value,
                            addreesViewModel.sodienthoai.value,
                            addreesViewModel.street_name.value,
                            addreesViewModel.district.value,
                            addreesViewModel.city.value,
                            "Việt Nam",
                            messageToShop
                        )

                        addreesViewModel.updateExistingAddress(
                            addreesViewModel.id_shipping.value,
                            address
                        )
                    }
                }

//            val intent = Intent(context, OrderActivity::class.java).apply {
//                putExtra("amount", grandPrice)
//                putExtra("id_shipping", placeOrderViewModel.cart_shipping)
//                putExtra("fee_transport", placeOrderViewModel.fee_transport)
//                placeOrderViewModel.selectedDiscount?.let { putExtra("discount_code", it.id) }
//                putExtra("makh", addreesViewModel.idkh.value)
//                putExtra("ctpxList", ArrayList(placeOrderViewModel.ctpxList))
//
//            }

//            context.startActivity(intent)

//            launcher.launch(intent)

                scope.launch {
                    try {
                        val response = if (selectedPayment.type == "cash") {
                            null
                        } else {
                            QuanlydienthoaiRepo.getUrlMomo(grandPrice, selectedPayment.type)
                        }
                        // Sau đó mở WebViewActivity truyền payUrl vào
                        val intent = Intent(context, MomoWebViewActivity::class.java).apply {
                            putExtra("payUrl", response)


                            putExtra("amount", grandPrice)
                            putExtra("id_shipping", placeOrderViewModel.cart_shipping)
                            putExtra("fee_transport", placeOrderViewModel.fee_transport)
                            placeOrderViewModel.selectedDiscount?.let {
                                putExtra(
                                    "discount_code",
                                    it.id
                                )
                            }
                            putExtra("makh", addreesViewModel.idkh.value)
                            putExtra("ctpxList", ArrayList(placeOrderViewModel.ctpxList))
                        }


                        launcher.launch(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Không thể tạo thanh toán MoMo", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            // Địa chỉ
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.clickable {
                addreesViewModel.isSelected.value = true
                navController.navigate(Screen.Account.Address.route)
            }) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFFF5722),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                if (addreesViewModel.hovaten.value.isEmpty() && addreesViewModel.addresss.isNotEmpty()) {

                    val address = addreesViewModel.addresss[0]

                    Column {
                        Text(
                            "${address.hovaten} ${formatPhoneNumber(address.sodienthoai)}",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "${address.street_name}, ${address.district}, ${address.city}, ${address.country}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    Column {
                        Text(
                            "${addreesViewModel.hovaten.value} ${formatPhoneNumber(addreesViewModel.sodienthoai.value)}",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "${addreesViewModel.street_name.value}, ${addreesViewModel.district.value}, ${addreesViewModel.city.value}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Sản phẩm

            Log.d("FGF", userCart.toString())

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 1000.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    var i = 0
                    var ctpxList: List<chitietphieuxuat> = emptyList()

                    userCart.forEach { (sanpham, phienbansanphams) ->

                        phienbansanphams.forEach { pb ->
                            val imageArray = sanpham.hinhanh.split(",").map { it.trim() }

                            Spacer(Modifier.height(10.dp))

                            Column {
                                Text(
                                    sanpham.tensp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Red,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageArray[0]), // Thay bằng ảnh sản phẩm thật
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            sanpham.sortDesc.toString(),
                                            fontSize = 14.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            if (pb.ram == null) {
                                                "null-null-" + pb.mausac
                                            } else {
                                                pb.ram + " GB-" + pb.rom + " GB-" + pb.mausac
                                            },
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black,
                                            fontSize = 15.sp
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                formatCurrency(pb.price_sale),
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            if (pb.price_sale != pb.giaxuat) {
                                                Text(
                                                    formatCurrency(pb.giaxuat),
                                                    fontWeight = FontWeight.Thin,
                                                    textDecoration = TextDecoration.LineThrough,
                                                    color = Color.Gray,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }

                                    cartItemList.forEach { cartItem ->

                                        if (cartItem.maphienbansp == pb.maphienbansp) {
                                            Text(
                                                "x" + cartItem.soluong,
                                                fontSize = 13.sp,
                                                color = Color.Gray
                                            )

                                            ctpxList += chitietphieuxuat(
                                                -1,
                                                cartItem.maphienbansp,
                                                "",
                                                cartItem.soluong,
                                                pb.price_sale.toInt(),
                                                tenSP = sanpham.tensp
                                            )

                                        }
                                    }

                                    placeOrderViewModel.ctpxList = ctpxList
                                }
                            }
                            i++
                        }
                    }
                }

            }
            Spacer(Modifier.height(12.dp))

            // Bảo hiểm
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(
//                        checked = false,
//                        onCheckedChange = {},
//                        colors = CheckboxDefaults.colors(uncheckedColor = Color.Gray)
//                    )
//                    Text("Bảo hiểm Thời trang", fontSize = 14.sp)
//                    Spacer(modifier = Modifier.weight(1f))
//                    Text("₫1.199", fontSize = 14.sp, color = Color.Gray)
//                }
//                Text(
//                    "Bảo vệ sản phẩm được bảo hiểm khỏi thiệt hại do sơ suất bất ngờ, tiếp xúc với chất lỏng hoặc hỏng trong quá trình sử dụng.",
//                    fontSize = 12.sp,
//                    color = Color.Gray,
//                    modifier = Modifier.padding(start = 44.dp)
//                )


            Spacer(Modifier.height(24.dp))

            // Phương thức vận chuyển
            Text("Phương thức vận chuyển", fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Nhanh", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(8.dp))
                        Text("₫200.000", color = Color.Gray)
                    }
                    Spacer(Modifier.height(4.dp))

                    val formatter = remember {
                        DateTimeFormatter.ofPattern("d 'Tháng' M")
                    }

                    val today = remember { LocalDate.now() }
                    val startDate = today.plusDays(5)
                    val endDate = today.plusDays(6)

                    val dateRange = "Đảm bảo nhận hàng từ ${startDate.format(formatter)} - ${
                        endDate.format(formatter)
                    }"

                    Text(
                        dateRange,
                        fontSize = 13.sp,
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Medium
                    )
//                    Text(
//                        "Nhận Voucher trị giá ₫15.000 nếu đơn hàng được giao đến bạn sau ngày 15 Tháng 4 2025.",
//                        fontSize = 12.sp,
//                        color = Color.Gray
//                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .clickable {
                        showSheet = true
                        scope.launch { sheetState.show() }
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Voucher của Shop", fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Text(vouncherSelect.ifBlank { "Chọn hoặc nhập mã" }, color = Color.Gray)
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
                    .clickable {
                        showMessageSheet = true
                        scope.launch { messageSheetState.show() }
                    }, verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Lời nhắn cho Shop", fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Text(
                    messageToShop.ifBlank {
                        messageToShopConst.ifBlank { "Để lại lời nhắn" }
                    },
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }

            Divider()

            Spacer(Modifier.height(24.dp))

            Text("Phương thức thanh toán", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                PaymentMethod.entries.forEach { method ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPayment = method }
                        .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = method.icon,
                            contentDescription = null,
                            tint = when (method) {
                                PaymentMethod.MOMO -> Color(0xFFD81B60)
                                PaymentMethod.ZALOPAY -> Color(0xFF2196F3)
                                else -> Color(0xFF4CAF50)
                            },
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(method.label, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.weight(1f))
                        RadioButton(
                            selected = selectedPayment == method,
                            onClick = { selectedPayment = method },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFFF5722)
                            )
                        )
                    }

                    if (method != PaymentMethod.ZALOPAY) {
                        Divider(thickness = 0.8.dp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Chi tiết thanh toán", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {

                Row {
                    Text("Tổng tiền hàng")
                    Spacer(Modifier.weight(1f))
                    Text(formatCurrency(totalPrice))
                }
                Spacer(Modifier.height(8.dp))

                var totalTransport = viewModel.totalTransport.collectAsState().value

                if (totalTransport > 0) {
                    placeOrderViewModel.fee_transport = 1
                }
                Row {
                    Text("Tổng phí vận chuyển")
                    Spacer(Modifier.weight(1f))
                    Text(formatCurrency(totalTransport))
                }
                Spacer(Modifier.height(8.dp))
                Divider()
                Spacer(Modifier.height(8.dp))


                Row {
                    Text("Tổng thanh toán (đã bao gồm VAT)", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text(formatCurrency(grandPrice), fontWeight = FontWeight.Bold)
                }
            }



            Spacer(Modifier.height(50.dp)) // Tránh bị che bởi bottom bar

            SuccessDialog(
                openDialog = placeOrderViewModel.isNotValidAddress,
                "Không hợp lệ  !!",
                "Vui lòng thêm địa chỉ để nhận hàng",
                "Confirm"
            ) {
                placeOrderViewModel.isNotValidAddress.value = false
            }
        }
    }
}

@Composable
fun CheckoutBottomBar(total: Double, onOrderClick: () -> Unit) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Tổng cộng", fontSize = 14.sp)
                Text(
                    text = formatCurrency(amount = total),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Button(
                onClick = onOrderClick,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                modifier = Modifier
                    .height(45.dp)
                    .width(110.dp)
            ) {
                Text("Đặt hàng", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun VoucherPicker(
    onVoucherSelected: (Discount) -> Unit,
    discounts: List<Discount>,
    amount: Double
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Chọn Voucher", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))

        // Danh sách voucher mẫu
        val vouchers = discounts
        vouchers.forEach { code ->

            val isAvailable =
                (amount >= code.paymentLimit) &&
                        (code.numberUsed > 1) &&
                        (code.expirationDate.after(Date()))
            //after con han , before het han

            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .then(
                        if (isAvailable)
                            Modifier.clickable { onVoucherSelected(code) }
                        else Modifier
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAvailable) Color.White else Color(0xFFF2F2F2)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .alpha(if (isAvailable) 1f else 0.5f), // Làm mờ nội dung nếu không dùng được
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = if (isAvailable) Color.Red else Color.Gray
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Mã: ${code.code}", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Giảm tới: ${formatCurrency(code.discountAmount.toDouble())}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isAvailable) Color.Red else Color.Gray
                        )
                        Text(
                            code.description,
                            fontSize = 13.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (!isAvailable) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Không áp dụng", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

        }
    }
}

@Composable
fun MessageInputSheet(initialText: String, onDone: (String) -> Unit) {
    var text by remember { mutableStateOf(initialText) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Lời nhắn cho Shop", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Nhập lời nhắn...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 3
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onDone(text) },
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
        ) {
            Text("Xong", color = Color.White)
        }
    }
}

enum class PaymentMethod(val label: String, val type: String, val icon: ImageVector) {
    COD("Thanh toán khi nhận hàng", "cash", Icons.Default.Money),
    MOMO(
        "Thanh toán bằng thẻ ngân hàng", "atm", Icons.Default.AccountBalanceWallet
    ),
    ZALOPAY("Thanh toán bằng thẻ visa", "visa", Icons.Default.Payment)
}

fun formatPhoneNumber(phone: String): String {
    val cleaned = phone.filter { it.isDigit() }
    return if (cleaned.startsWith("0") && cleaned.length == 10) {
        val national = cleaned.drop(1) // Bỏ số 0 đầu
        val formatted =
            "${national.substring(0, 3)} ${national.substring(3, 6)} ${national.substring(6)}"
        "(+84) $formatted"
    } else {
        phone
    }
}



