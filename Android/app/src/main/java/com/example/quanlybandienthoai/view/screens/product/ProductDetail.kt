package com.example.quanlybandienthoai.view.screens.product

import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.view.components.AnimatedLoadingIndicator
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.TopBar
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.ProductViewModel
import com.example.quanlybandienthoai.worker.SyncFirebaseToApiWorker
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.ceil

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductDetailsScreen(
    id: Int,
    navController: NavController,
    homeViewModel: HomeViewModel,
    context: Application = LocalContext.current.applicationContext as Application,
    productViewModel: ProductViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    val product by productViewModel.product.collectAsState()
    val reviews by productViewModel.reviews.collectAsState()

    val userId = appViewModel.getCurrentUserId()

    LaunchedEffect(userId) {
        val inputData = workDataOf("userId" to userId)

        val workRequest = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    LaunchedEffect(Unit) {
        productViewModel.setCurrentProduct(id.toString())
    }

    val phienBanList = product.let { homeViewModel.phienBanMap[it.masp] } ?: emptyList()


    LaunchedEffect(product) {
        homeViewModel.getPhienbansanpham(product.masp)

        productViewModel.getReviewsByProduct(product.masp)
    }

    if (productViewModel.isLoadingDetail) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedLoadingIndicator(productViewModel.isLoadingDetail)
        }
    } else if (phienBanList.isNotEmpty()) {
        var selectedStorage =
            remember { phienBanList.first().rom?.toIntOrNull() ?: 0 }


        var selectedRam =
            remember { phienBanList.first().ram?.toIntOrNull() ?: 0 }

        val selectedColor = remember { mutableStateOf(phienBanList.first().mausac) }

        val quantity = remember { mutableIntStateOf(1) }
        val price_sale = remember { mutableStateOf(0.0) }
        val giaxuat = remember { mutableStateOf(0.0) }
        val coroutineScope = rememberCoroutineScope()

        var mapb = remember { phienBanList.first().maphienbansp }



        LaunchedEffect(phienBanList) {
            price_sale.value = phienBanList[0].price_sale
            giaxuat.value = phienBanList[0].giaxuat
        }


        Scaffold(
            topBar = {
                TopBar(product.tensp, false, navBack = {
                    navController.popBackStack()
                    val detailScreen = Screen.ProductDetail(product.masp)


                    homeViewModel.colorListEqualRom.clear()
                    mainViewModel.deleteScreenInBottom(detailScreen)
                    mainViewModel.updateSelectedScreen(navController)
                })

            }, bottomBar = {
                BottomNavigationBar(navController, mainViewModel)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val imageArray = product.hinhanh.split(",").map { it.trim() }
                val selectedImage = remember { mutableStateOf(imageArray[0]) }

                LaunchedEffect(product) {
                    selectedImage.value = imageArray[0] // Cập nhật ảnh mới khi sản phẩm thay đổi
                }

                AnimatedContent(
                    targetState = selectedImage.value,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) with fadeOut(animationSpec = tween(500))
                    },
                    label = "Product Image Transition"
                ) { targetImage ->
                    Image(
                        painter = rememberAsyncImagePainter(targetImage),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(imageArray) { image ->
                        val isSelected = selectedImage.value == image
                        val imageSize by animateDpAsState(
                            targetValue = if (isSelected) 80.dp else 65.dp,
                            animationSpec = tween(durationMillis = 300),
                            label = "Thumbnail Size Animation"
                        )

                        Image(
                            painter = rememberAsyncImagePainter(image),
                            contentDescription = "Product Thumbnail",
                            modifier = Modifier
                                .size(imageSize)
                                .clip(RoundedCornerShape(8.dp))
                                .padding(7.dp)
                                .clickable {
                                    if (!isSelected) {
                                        selectedImage.value = image
                                    }
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Thông tin sản phẩm
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = product.tensp,
                        fontSize = 35.sp,
                        fontFamily = fontFamily(),
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Đã bán ${product.soluongban}",
                        color = Color.Gray,
                        fontFamily = fontFamily(),
                        fontWeight = FontWeight.Light,
                        fontSize = 15.sp
                    )

                    // Giá sản phẩm
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatCurrency(price_sale.value),
                            color = Color.Red,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (price_sale.value != giaxuat.value) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatCurrency(giaxuat.value),
                                color = Color.Gray,
                                fontSize = 15.sp,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val rating = product.soluongdanhgia.takeIf { it > 0 }
                        ?.let { ceil((product.tongsao / it).toDouble()).toInt() } ?: 0

                    // Đánh giá
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        for (i in 1..5 step (1)) {
                            if (rating >= i) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating Star",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(17.dp)
                                )
                            } else if (rating >= i - 0.5) {
                                Icon(
                                    imageVector = Icons.Default.StarHalf,
                                    contentDescription = "Rating Star",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(17.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.StarBorder,
                                    contentDescription = "Rating Star",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mô tả sản phẩm
                    product.sortDesc?.let {
                        Text(
                            text = it,
                            fontFamily = fontFamily(),
                            fontWeight = FontWeight.ExtraLight,
                            fontSize = 19.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Chọn dung lượng:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(phienBanList) { phienban ->
                            if (selectedStorage != 0) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (selectedStorage == (phienban.rom?.toIntOrNull()
                                                    ?: 0) && mapb == (phienban.maphienbansp)
                                            )
                                                Color(
                                                    0xFFB0E0E6
                                                )
                                            else Color.White
                                        )
                                        .border(
                                            1.dp,
                                            if (selectedStorage == (phienban.rom?.toIntOrNull()
                                                    ?: 0) && mapb == (phienban.maphienbansp)
                                            )
                                                Color(
                                                    0xFF87CEEB
                                                ) else Color.Black,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .clickable {
                                            selectedStorage = phienban.rom?.toIntOrNull() ?: 0
                                            selectedRam = phienban.ram?.toIntOrNull() ?: 0

                                            coroutineScope.launch {
                                                if (phienban.rom != null) {
                                                    homeViewModel.getPriceByRom(
                                                        product.masp,
                                                        phienban.maphienbansp,
                                                        phienban.rom!!.toInt()
                                                    ) { (tem1, tem2) ->
                                                        price_sale.value = tem1.toDouble()
                                                        giaxuat.value = tem2.toDouble()
                                                    }
                                                }

                                                mapb = phienban.maphienbansp
                                            }
                                        }
                                ) {
                                    Text(
                                        text = if ((phienban.rom?.toIntOrNull()
                                                ?: 0) == 1000
                                        ) "1 TB" else "${phienban.rom} GB",
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (selectedStorage != 0) {
                        LaunchedEffect(product.masp, mapb) {
                            homeViewModel.colorListEqualRom.clear()
                            selectedColor.value = ""
                            if (selectedStorage != 0) {
                                homeViewModel.getListColor(
                                    masp = product.masp,
                                    mapb = mapb
                                )
                            }
                        }
                    }

                    val colorMap: Map<String, Color> = mapOf(
                        "màu đen" to Color.Black,
                        "màu trắng" to Color.White,
                        "màu đỏ" to Color.Red,
                        "màu xanh dương" to Color.Blue,
                        "màu xanh lá" to Color.Green,
                        "màu vàng" to Color.Yellow,
                        "màu xám" to Color.Gray,
                        "màu tím" to Color(0xFF800080), // Mã màu tím
                        "màu cam" to Color(0xFFFFA500), // Mã màu cam
                        "màu hồng" to Color(0xFFFFC0CB)  // Mã màu hồng
                    )

                    // Chọn màu sắc
                    Text(text = "Chọn màu sắc:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (homeViewModel.colorListEqualRom.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(homeViewModel.colorListEqualRom.distinct()) { color -> // Xóa màu trùng lặp
                                val colorValue =
                                    colorMap[color.lowercase(Locale.getDefault())] ?: Color.Red

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colorValue)
                                        .border(
                                            4.dp,
                                            if (selectedColor.value == color) Color(0xFF87CEEB) else Color.Transparent,
                                            CircleShape
                                        )
                                        .clickable { selectedColor.value = color }
                                )
                            }
                        }
                    } else if (phienBanList[0].rom == null) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            item {
                                val colorValue = colorMap[selectedColor.value] ?: Color.Red

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colorValue)
                                        .border(
                                            4.dp,
                                            Color(0xFF87CEEB),
                                            CircleShape
                                        )
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    // Chọn số lượng
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        IconButton(onClick = { if (quantity.intValue > 1) quantity.intValue-- }) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }
                        Text(
                            text = quantity.intValue.toString(),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IconButton(onClick = { quantity.intValue++ }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }


                    Spacer(modifier = Modifier.height(8.dp))
                    // Nút mua hàng
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // WTF
//                        Button(
//                            onClick = { /* Mua ngay */ },
//                            colors = ButtonDefaults.buttonColors(Color.Red),
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text("Mua ngay", color = Color.White)
//                        }

//                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {

                                //update to Mysql
                                val inputData = workDataOf("userId" to userId)

                                val workRequest = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
                                    .setInputData(inputData)
                                    .build()

                                WorkManager.getInstance(context).enqueue(workRequest)

                                Log.d("check", mapb.toString())
                                Log.d("check", selectedStorage.toString())

                                Log.d("check", selectedRam.toString())


                                cartViewModel.checkCartIdKH(
                                    appViewModel.getCurrentUserId().toInt()
                                ) { exist ->
                                    Log.d("CartCheck", "Cart exist: $exist")

                                    val cartId = cartViewModel.cartId.value
                                    Log.d("CartId", "Cart ID: $cartId")

                                    val cartItem = CartItem(
                                        -1,
                                        -1,
                                        mapb,
                                        product.masp,
                                        quantity.intValue
                                    )

                                    var pb = phienBanList[0]

                                    if (phienBanList[0].price_sale != price_sale.value) {
                                        phienBanList.forEach { phienban ->
                                            if (phienban.maphienbansp == mapb) {
                                                pb = phienban
                                            }
                                        }
                                    }

                                    if (exist) {
                                        cartViewModel.insertCartItem(cartId, cartItem, product, pb)
                                    } else {
                                        cartViewModel.insertCart(Cart(-1, 2), cartItem, product, pb)
                                    }
                                }


//                                var pb = phienBanList[0]
//
//                                phienBanList.forEach { phienban ->
//                                    if (phienban.maphienbansp == mapb) {
//                                        pb = phienban
//                                    }
//                                }
//
//                                cartViewModel.addToCart(product, pb, quantity.intValue)
                            },
                            colors = ButtonDefaults.buttonColors(Color.Blue),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Thêm vào giỏ", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val selectedStorageSize = remember(selectedStorage) {
                        if (selectedStorage != 0) "${selectedStorage} GB" else "Không xác định"
                    }

                    val ramSize =
                        if (selectedRam != 0) "${selectedRam} GB" else "Không xác định"

                    val technicalSpecs: List<Pair<String, String?>> = listOf(
                        "Màn hình" to product.manhinh,
                        "Hệ điều hành" to product.hedieuhanh,
                        "Camara sau" to product.camerasau,
                        "Camara trước" to product.cameratruoc,
                        "RAM" to ramSize,
                        "Bộ nhớ trong" to selectedStorageSize,
                        "Dung lượng pin" to product.dungluongpin.toString()
                    )

// Hiển thị bảng thông số kỹ thuật
                    TechnicalSpecsTable(specs = technicalSpecs)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Luôn cập nhật detail khi product thay đổi
                    val detail = remember { mutableStateOf(product.detail) }

                    LaunchedEffect(product.detail) {
                        detail.value = product.detail
                    }

                    detail.value?.let { HtmlDescription(it) }

                    Spacer(modifier = Modifier.height(10.dp))

//                    LaunchedEffect(product.masp) {
//                        productViewModel.getReviewsByProduct(productId = product.masp)
//                    }

                    if (productViewModel.isLoadingComment) {
                        AnimatedLoadingIndicator(productViewModel.isLoadingComment)
                    } else if (reviews.isEmpty()) {
                        Text("Sản phẩm chưa có bình luận nào", color = Color.Red, fontSize = 15.sp)
                    } else {
                        ProductReviewsSection(
                            reviews,
                            product.soluongdanhgia,
                            product.tongsao
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Log.d("title", homeViewModel.titleListCurrent)
                    if (homeViewModel.titleListCurrent.isNotEmpty()) {
                        ProductSimilar(
                            "",
                            product.masp,
                            navController,
                            mainViewModel,
                            homeViewModel
                        )
                    } else {
                        val promoLabels = mapOf(
                            "moiramat" to "🆕 Sản phẩm mới",
                            "tragop" to "💳 Trả góp 0 %",
                            "giareonline" to "💥 Giá sốc online",
                            "giamgia" to "🔻 Giảm giá lớn"
                        )

                        val promoName = product.promo.firstOrNull()?.get("name") as? String

                        val label = promoLabels[promoName] ?: "🔥 Nổi bật nhất"

                        ProductSimilar(
                            label,
                            product.masp,
                            navController,
                            mainViewModel,
                            homeViewModel
                        )
                    }
                }
            }
        }
    }
}


//@Composable
//fun ProductDetail(
//    navController: NavController,
//    productViewModel1: ProductViewModel1,
//    commentViewModel: CommentViewModel,
//    productId: Int
//) {
//    val comments by commentViewModel.listCommentData.observeAsState(initial = emptyList())
//    val phoneData by productViewModel1.phoneData.observeAsState(initial = null)
//
//
//    LaunchedEffect(Unit) {
//        commentViewModel.getListCommentData(productId)
//        productViewModel1.selectPhone(productId)
//    }
//
//    Box(
//        modifier = Modifier
//            .padding(20.dp)
//            .fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            // 🔹 Phần Name - Luôn dính trên đầu
//            phoneData?.let {
//                Text(
//                    text = it.name,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 10.dp),
//                    fontSize = 30.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Left
//                )
//            }
//
//            Row {
//
//                Image(
//                    painter = painterResource(R.drawable.start),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .background(Color.White)
//                        .size(20.dp)
//                )
//
//                Text(
//                    text = phoneData?.star.toString(),
//                    modifier = Modifier.height(17.dp),
//                    fontSize = 15.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Left
//                )
//
//                Text(
//                    text = "(" + phoneData?.totalReview.toString() + " Reviews)",
//                    modifier = Modifier.height(17.dp),
//                    fontSize = 15.sp,
//                    fontWeight = FontWeight.Normal
//                )
//
//            }
//
//
//            // 🔹 Phần nội dung có thể scroll
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 20.dp, bottom = 100.dp)
//            ) {
//                item {
//                    phoneData?.let { painterResource(it.img) }?.let {
//                        Image(
//                            painter = it,
//                            contentDescription = null,
//                            contentScale = ContentScale.Fit,
//                            modifier = Modifier
//                                .background(Color.White)
//                                .fillMaxWidth()
//                                .height(300.dp),
//                        )
//                    }
//
//                    phoneData?.let {
//                        Text(
//                            text = it.description,
//                            modifier = Modifier
//                                .padding(vertical = 10.dp)
//                                .fillMaxWidth(),
//                            fontSize = 15.sp,
//                            lineHeight = 18.sp,
//                            textAlign = TextAlign.Left
//                        )
//                    }
//
//                    Divider(thickness = 1.dp, color = Color.Gray)
//
//                    Text(
//                        text = "Reviews",
//                        modifier = Modifier.padding(vertical = 8.dp),
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                items(comments) { comment ->
//                    CommentCard(comment)
//                }
//
//            }
//        }
//
//        // 🔹 Phần Row cuối - Luôn dính dưới
//        Row(
//            modifier = Modifier
//                .align(Alignment.BottomCenter) // Dính xuống dưới
//                .fillMaxWidth()
//                .background(Color.White)
//                .padding(10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
////            Text(
////                text =  phoneData?.price?.let { formatCurrency(it) } ?: "N/A",
////                fontSize = 17.sp,
////                fontWeight = FontWeight.Bold,
////                textAlign = TextAlign.Left,
////                lineHeight = 18.sp,
////            )
//
//            Button(
//                border = BorderStroke(2.dp, Color.Transparent),
//                onClick = { /* Xử lý khi click */ }
//            ) {
//                Text(
//                    text = "Mua ngay",
//                    fontSize = 17.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CommentCard(commentData: CommentData) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(12.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        imageVector = Icons.Filled.AccountCircle,
//                        contentDescription = "User Icon",
//                        tint = Color.Gray,
//                        modifier = Modifier
//                            .size(20.dp)
//                            .background(Color.Transparent)
//                    )
//
//                    Spacer(modifier = Modifier.width(6.dp))
//
//                    Text(
//                        text = commentData.name,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                Text(
//                    text = commentData.date,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(6.dp))
//
//            // Hiển thị số sao
//            Row {
//                repeat(commentData.star) {
//                    Image(
//                        painter = painterResource(R.drawable.start),
//                        contentDescription = "Star",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(16.dp)
//                            .background(Color.Transparent)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(6.dp))
//
//            Text(
//                text = commentData.title,
//                fontSize = 13.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Text(
//                text = commentData.content,
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Normal
//            )
//        }
//    }
//}
