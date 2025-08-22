package com.example.quanlybandienthoai.view.screens.home

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.Slide
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.view.components.AnimatedLoadingIndicator
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.components.TopBar
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.FilterViewModel
import com.example.quanlybandienthoai.viewmodel.FilterViewModelFactory
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModelFactory
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.SlideViewModel
import com.example.quanlybandienthoai.worker.SyncFirebaseToApiWorker
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    context: Application = LocalContext.current.applicationContext as Application,
    slideViewModel: SlideViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
    filterViewModel: FilterViewModel = viewModel(
        factory = FilterViewModelFactory(
            context,
            homeViewModel = homeViewModel
        )
    ),
    cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity)
) {


    val userId = appViewModel.getCurrentUserId()

//    LaunchedEffect(userId) {
//        val inputData = workDataOf("userId" to userId)
//
//        val workRequest = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
//            .setInputData(inputData)
//            .build()
//
//        WorkManager.getInstance(context).enqueue(workRequest)
//    }

    val LoraFontFamily = fontFamily()

    LaunchedEffect(Unit) {
        mainViewModel.updateSelectedScreen(navController)
    }


    //http://localhost:8080/Spring-mvc/quan-tri/san-pham
//    val sampleSanphamList = listOf(
//        Sanpham(
//            masp = 1,
//            tensp = "Samsung Galaxy S25 Ultra",
//            hinhanh = "https://img.upanh.tv/2025/03/21/download.jpg",
//            xuatxu = 1, // 1: Việt Nam, 2: Hàn Quốc, v.v.
//            dungluongpin = 5000.0,
//            manhinh = "6.8 inch Dynamic AMOLED 2X",
//            hedieuhanh = 1, // 1: Android, 2: iOS, v.v.
//            phienbanhdh = 14.0,
//            camerasau = "200MP + 10MP + 10MP + 12MP",
//            cameratruoc = "12MP",
//            thuonghieu = 1, // 1: Samsung, 2: Apple, v.v.
//            khuvuckho = 3, // 3: Hà Nội
//            soluongnhap = 100,
//            soluongban = 20,
//            promo = listOf(
//                mapOf("name" to "giamgia", "value" to 10),
//            ),
//            sortDesc = "Siêu phẩm mới nhất từ Samsung",
//            detail = "Điện thoại flagship với chip Snapdragon 8 Gen 3, bút S-Pen và camera 200MP.",
//            tongsao = 1200,
//            soluongdanhgia = 400,
//            created = "2024-03-20",
//            trash = "0",
//            status = 1
//
//        ),
//        Sanpham(
//            masp = 2,
//            tensp = "iPhone 16 Pro Max",
//            hinhanh = "https://res.cloudinary.com/deynh1vvv/image/upload/v1742550398/gzlkua6l6dxowv3spmye.webp",
//            xuatxu = 2,
//            dungluongpin = 4850.0,
//            manhinh = "6.7 inch Super Retina XDR",
//            hedieuhanh = 2,
//            phienbanhdh = 17.0,
//            camerasau = "48MP + 12MP + 12MP",
//            cameratruoc = "12MP",
//            thuonghieu = 2,
//            khuvuckho = 1,
//            soluongnhap = 150,
//            soluongban = 50,
//            promo = listOf(
//                mapOf("name" to "giareonline", "value" to 10),
//
//                ),
//            sortDesc = "Siêu phẩm Apple 2024",
//            detail = "Trang bị chip A17 Pro, vỏ titanium, camera 48MP.",
//            tongsao = 320,
//            soluongdanhgia = 90,
//            created = "2024-02-15",
//            trash = "0",
//            status = 1
//        ),
//        Sanpham(
//            masp = 3,
//            tensp = "Nokia",
//            hinhanh = "https://img.upanh.tv/2025/03/21/download5072490618bb199d.jpg",
//            xuatxu = 2,
//            dungluongpin = 4850.0,
//            manhinh = "6.7 inch Super Retina XDR",
//            hedieuhanh = 2,
//            phienbanhdh = 17.0,
//            camerasau = "48MP + 12MP + 12MP",
//            cameratruoc = "12MP",
//            thuonghieu = 2,
//            khuvuckho = 1,
//            soluongnhap = 150,
//            soluongban = 50,
//            promo = listOf(
//                mapOf("name" to "tragop", "value" to 0),
//                ),
//            sortDesc = "Siêu phẩm Apple 2024",
//            detail = "Trang bị chip A17 Pro, vỏ titanium, camera 48MP.",
//            tongsao = 0,
//            soluongdanhgia = 0,
//            created = "2024-02-15",
//            trash = "0",
//            status = 1
//        ),
//        Sanpham(
//            masp = 4,
//            tensp = "Mobistar",
//            hinhanh = "https://img.upanh.tv/2025/03/21/imagesdc21d8c9346d436f.jpg",
//            xuatxu = 2,
//            dungluongpin = 4850.0,
//            manhinh = "6.7 inch Super Retina XDR",
//            hedieuhanh = 2,
//            phienbanhdh = 17.0,
//            camerasau = "48MP + 12MP + 12MP",
//            cameratruoc = "12MP",
//            thuonghieu = 2,
//            khuvuckho = 1,
//            soluongnhap = 150,
//            soluongban = 50,
//            promo =  listOf(
//                mapOf("name" to "moiramat", "value" to 0),
//            ),
//            sortDesc = "Siêu phẩm Apple 2024",
//            detail = "Trang bị chip A17 Pro, vỏ titanium, camera 48MP.",
//            tongsao = 50,
//            soluongdanhgia = 20,
//            created = "2024-02-15",
//            trash = "0",
//            status = 1
//        ),
//        Sanpham(
//            masp = 5,
//            tensp = "Xioami",
//            hinhanh = "https://img.upanh.tv/2025/03/21/downloade3e678256a9106c3.jpg",
//            xuatxu = 2,
//            dungluongpin = 4850.0,
//            manhinh = "6.7 inch Super Retina XDR",
//            hedieuhanh = 2,
//            phienbanhdh = 17.0,
//            camerasau = "48MP + 12MP + 12MP",
//            cameratruoc = "12MP",
//            thuonghieu = 2,
//            khuvuckho = 1,
//            soluongnhap = 150,
//            soluongban = 50,
//            promo =  listOf(
//                mapOf("name" to "giareonline", "value" to 10),
//                mapOf("name" to "giamgia", "value" to 10),
//            ),
//            sortDesc = "Siêu phẩm Apple 2024",
//            detail = "Trang bị chip A17 Pro, vỏ titanium, camera 48MP.",
//            tongsao = 3020,
//            soluongdanhgia = 990,
//            created = "2024-02-15",
//            trash = "0",
//            status = 1
//        )
//    )
//
//    val ssModel = listOf(
//        Phienbansanpham(
//            masp = 1,
//            maphienbansp = 101,
//            rom = 256,
//            ram = 12,
//            mausac = 1, // 1: Đen
//            gianhap = 25000000.0,
//            giaxuat = 29990000.0,
//            soluongton = 50,
//            sale = 0.0, // Giảm 10%
//            price_sale = 29990000.0
//        ),
//        Phienbansanpham(
//            masp = 1,
//            maphienbansp = 102,
//            rom = 512,
//            ram = 12,
//            mausac = 2, // 2: Xanh
//            gianhap = 27000000.0,
//            giaxuat = 32990000.0,
//            soluongton = 30,
//            sale = 0.0, // Giảm 15%
//            price_sale = 32990000.0
//        )
//    )

//
//    val ipModel = listOf(
//        // Phiên bản cho iPhone 15 Pro Max
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 201,
//            rom = 256,
//            ram = 8,
//            mausac = 3, // 3: Titan xanh
//            gianhap = 28000000.0,
//            giaxuat = 33990000.0,
//            soluongton = 40,
//            sale = 10.0, // Giảm 5%
//            price_sale = 33990000.0* (1 - 10 / 100.0)
//        ),
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 202,
//            rom = 512,
//            ram = 8,
//            mausac = 4, // 4: Titan trắng
//            gianhap = 31000000.0,
//            giaxuat = 37990000.0,
//            soluongton = 25,
//            sale = 10.0, // Giảm 7%
//            price_sale = 37990000.0* (1 - 10 / 100.0)
//        )
//    )
//
//    val nokia = listOf(
//        // Phiên bản cho iPhone 15 Pro Max
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 301,
//            rom = 0,
//            ram = 8,
//            mausac = 3, // 3: Titan xanh
//            gianhap = 28000000.0,
//            giaxuat = 390000.0,
//            soluongton = 40,
//            sale = 0.0, // Giảm 5%
//            price_sale = 390000.0
//        ),
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 301,
//            rom = 0,
//            ram = 8,
//            mausac = 3, // 3: Titan xanh
//            gianhap = 28000000.0,
//            giaxuat = 1990000.0,
//            soluongton = 40,
//            sale = 0.0, // Giảm 5%
//            price_sale = 1990000.0
//        )
//    )
//
//    val mobi = listOf(
//        // Phiên bản cho iPhone 15 Pro Max
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 401,
//            rom = 0,
//            ram = 8,
//            mausac = 3, // 3: Titan xanh
//            gianhap = 28000000.0,
//            giaxuat = 1990000.0,
//            soluongton = 40,
//            sale = 0.0, // Giảm 5%
//            price_sale = 1990000.0
//        ),
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 402,
//            rom = 0,
//            ram = 8,
//            mausac = 3, // 3: Titan xanh
//            gianhap = 28000000.0,
//            giaxuat = 200000.0,
//            soluongton = 40,
//            sale = 0.0, // Giảm 5%
//            price_sale = 200000.0
//        )
//    )
//
//    val xx = listOf(
//        // Phiên bản cho iPhone 15 Pro Max
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 201,
//            rom = 256,
//            ram = 8,
//            mausac = 3, // 3: Titan xanh
//            gianhap = 28000000.0,
//            giaxuat = 43990000.0,
//            soluongton = 40,
//            sale = 10.0, // Giảm 5%
//            price_sale = 43990000.0* (1 - 10 / 100.0)
//        ),
//        Phienbansanpham(
//            masp = 2,
//            maphienbansp = 202,
//            rom = 512,
//            ram = 8,
//            mausac = 4, // 4: Titan trắng
//            gianhap = 31000000.0,
//            giaxuat = 57990000.0,
//            soluongton = 25,
//            sale = 10.0, // Giảm 7%
//            price_sale = 57990000.0* (1 - 10 / 100.0)
//        )
//    )
//
//
//        homeViewModel.insertSanpham(sampleSanphamList[0], ssModel)
//        homeViewModel.insertSanpham(sampleSanphamList[1], ipModel)
//        homeViewModel.insertSanpham(sampleSanphamList[2], nokia)
//        homeViewModel.insertSanpham(sampleSanphamList[3], mobi)
//        homeViewModel.insertSanpham(sampleSanphamList[4], xx)


//    homeViewModel.highlightList.forEach { highlight ->
//        LaunchedEffect(highlight) {
//            homeViewModel.getProductsByHighlights(highlight)
//        }
//    }
//
//    val brands = listOf(
//        Thuonghieu("iPhone","https://res.cloudinary.com/deynh1vvv/image/upload/v1742580162/iphone.jpg"),
//        Thuonghieu("Xiaomi","https://res.cloudinary.com/deynh1vvv/image/upload/v1742580163/xiaomi.png"),
//        Thuonghieu("Samsung", "https://res.cloudinary.com/deynh1vvv/image/upload/v1742580163/samsung.jpg"),
//        Thuonghieu("Oppo", "https://res.cloudinary.com/deynh1vvv/image/upload/v1742580163/oppo.jpg"),
//        Thuonghieu("Nokia", "https://res.cloudinary.com/deynh1vvv/image/upload/v1742580163/nokia.jpg"),
//        Thuonghieu("Huawei", "https://res.cloudinary.com/deynh1vvv/image/upload/v1742580162/huawei.jpg"),
//        Thuonghieu("Realme","https://res.cloudinary.com/deynh1vvv/image/upload/v1742580163/realme.png"),
//        Thuonghieu("Vivo", "https://res.cloudinary.com/deynh1vvv/image/upload/v1742580163/vivo.jpg"),
//        Thuonghieu("Itel", "https://res.cloudinary.com/deynh1vvv/image/upload/v1742580162/intel.jpg"),
//        Thuonghieu("Coolpad","https://res.cloudinary.com/deynh1vvv/image/upload/v1742580162/coolpad.png"),
//        Thuonghieu("Mobistar","https://res.cloudinary.com/deynh1vvv/image/upload/v1742580163/mobistar.jpg"),
//        Thuonghieu("Mobell", "https://res.cloudinary.com/deynh1vvv/image/upload/v1742580162/mobell.jpg")
//    )
//
//    brands.forEach{brand->
//        homeViewModel.insertCategory(brand)
//    }


//    val slides = listOf(
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058794/banner8.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058794/banner7.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058794/banner6.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058794/banner5.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058794/banner4.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058795/banner9.gif"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1742580162/banner3.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1742580162/banner2.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058793/banner1.png"),
//        Slide("https://res.cloudinary.com/deynh1vvv/image/upload/v1743058793/banner0.png"),
//    )
//
//    slides.asReversed().forEach { slide ->
//        slideViewModel.insertSlide(slide)
//    }


//    LaunchedEffect(Unit) {
//        homeViewModel.getAllCategories()
//
//        slideViewModel.getAllSlides()
//    }
//
//
//    homeViewModel.highlightList.forEach { highlight ->
//        LaunchedEffect(highlight) {
//            homeViewModel.getProductsByHighlights(highlight)
//        }
//    }

//    LaunchedEffect(Unit) {
//        if (homeViewModel.categories.isEmpty()) {
//            homeViewModel.getAllCategories()
//        }
//
//        if (slideViewModel.slides.isEmpty()) {
//            slideViewModel.getAllSlides()
//        }
//
//        cartViewModel.setUser(appViewModel.getCurrentUser())
//    }

    val user = appViewModel.getCurrentUser()

    LaunchedEffect(user?.id) {
        if (homeViewModel.categories.isEmpty() || slideViewModel.slides.isEmpty()) {
            homeViewModel.getAllCategories()
            slideViewModel.getAllSlides()
        }

        if(user != null ){
            cartViewModel.setUser(user)
            Log.d("DebugCar",user.toString()+cartViewModel.userCart.value.toString())
        }
    }


    if (homeViewModel.highlightProductsMap.isEmpty()) {
        homeViewModel.highlightList.forEach { highlight ->
            LaunchedEffect(highlight) {
                if (homeViewModel.highlightProductsMap[highlight].isNullOrEmpty()) {
                    homeViewModel.getProductsByHighlights(highlight)
                }
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val prefs = remember { context.getSharedPreferences("prefs", MODE_PRIVATE) }

    val scrollPosition by homeViewModel.scrollPosition.collectAsState()

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition
    )

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .debounce(300L)
            .collectLatest { index ->
                homeViewModel.updateScrollPosition(index)
                Log.d("Scroll", "Lưu vị trí cuộn: $index")
            }
    }

    LaunchedEffect(Unit) {
        val savedScrollPosition = prefs.getInt("scroll_position", 0)
        Log.d("Scroll", "Khôi phục vị trí cuộn: $savedScrollPosition")

        snapshotFlow { homeViewModel.highlightProductsMap }
            .filter { map ->
                homeViewModel.highlightList.all { highlight ->
                    map[highlight]?.isNotEmpty() == true
                }
            }
            .firstOrNull()  // Chờ dữ liệu đầy đủ
            ?.let {
                lazyListState.scrollToItem(savedScrollPosition)
            }
    }



    Scaffold(
        topBar = {
            HomeTopBar(homeViewModel) { ten ->
                filterViewModel.selectFilter("Tên sản phẩm", ten)
            }

        }, bottomBar = {
            BottomNavigationBar(navController, mainViewModel)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) // Xanh nhạt dựa trên primary color
                .padding(paddingValues)
                .fillMaxSize(),
            state = lazyListState
        ) {
            item {
                Text(
                    "Xin chào " + appViewModel.getCurrentUser()?.firstName,
                    fontFamily = LoraFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )

                Text(
                    "Hãy cùng bắt đầu trải nghiệm mua sắm của bạn",
                    fontFamily = LoraFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp, start = 10.dp)
                )
            }
            item {
                if (slideViewModel.isLoadingSlide) {
                    AnimatedLoadingIndicator(slideViewModel.isLoadingSlide)
                } else {
                    BannerSlider(slideViewModel.slides)
                }
            }
            item {
                Text(
                    "Khám phá các thương hiệu",
                    fontFamily = LoraFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(bottom = 16.dp, start = 10.dp)
                )

                if (homeViewModel.isLoadingBrand) {
                    AnimatedLoadingIndicator(homeViewModel.isLoadingBrand)
                } else {
                    BrandCategoryGrid(
                        modifier = Modifier.height(120.dp),
                        homeViewModel.categories
                    ) { id, tenBrand ->
                        coroutineScope.launch {
                            // homeViewModel.getCategoryProducts(id.toString())
                            filterViewModel.selectFilter("Thương hiệu", tenBrand)
                        }
                    }

                    FilterScreen(filterViewModel, homeViewModel)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
            if (!homeViewModel.isFilter) {

                items(homeViewModel.highlightList) { highlight ->
                    if (!homeViewModel.isFilter) {
                        if (homeViewModel.isLoading) {
                            AnimatedLoadingIndicator(homeViewModel.isLoading)
                        } else {
                            ProductHighlights(
                                highlight,
                                navController,
                                mainViewModel,
                                homeViewModel, cartViewModel
                            )
                        }
                    }
                }
            } else {
                item {
                    if (homeViewModel.isLoading) {
                        AnimatedLoadingIndicator(homeViewModel.isLoading)
                    } else {
                        ProductGridFilter(
                            navController,
                            homeViewModel,
                            mainViewModel,
                            cartViewModel
                        )
                    }
                }
            }
        }

        val openRemoveCartItemDialog = remember { mutableStateOf(false) }


        if (cartViewModel.message.isNotEmpty()) {
            val inputData = workDataOf("userId" to userId)

            val workRequest = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)

            AlertDialog(
                onDismissRequest = {
                    openRemoveCartItemDialog.value = false
                    cartViewModel.message = ""
                },
                title = {
                    if (cartViewModel.message == "Thêm sản phẩm vào giỏ hàng thành công") {
                        Text(text = "Thêm vào giỏ thành công !!")
                    } else {
                        Text(text = cartViewModel.message)
                    }
                },
                text = {
                    cartViewModel.message
                },
                confirmButton = {
                    if (cartViewModel.message == "Thêm sản phẩm vào giỏ hàng thành công") {
                        Button(
                            onClick = {
                                openRemoveCartItemDialog.value = false
                                cartViewModel.message = ""
                                navController.navigate(Screen.Cart.route)

                                mainViewModel.updateSelectedScreen(navController)
                            }
                        ) {
                            Text("Tới giỏ hàng")
                        }
                    } else {
                        Button(
                            onClick = {
                                openRemoveCartItemDialog.value = false
                                cartViewModel.message = ""

                            }
                        ) {
                            Text("Xác nhận")
                        }
                    }

                },
                dismissButton = {
                    Button(
                        onClick = {
                            openRemoveCartItemDialog.value = false
                            cartViewModel.message = ""
                        }) {
                        Text("Cancel")
                    }
                }
            )
        }

        SuccessDialog(
            openDialog = appViewModel.isLoginSuccess,
            "Login success !!",
            "Xin chào " + appViewModel.getCurrentUser()?.firstName,
            "Confirm"
        ) {
            appViewModel.isLoginSuccess.value = false
        }

    }
}


fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(amount)
}

fun fontFamily(): FontFamily {
    return FontFamily(
        Font(R.font.lora_regular, FontWeight.Normal),
        Font(R.font.lora_medium, FontWeight.Medium),
        Font(R.font.lora_mediumitalic, FontWeight.ExtraLight),
        Font(R.font.lora_semibold, FontWeight.SemiBold),
        Font(R.font.lora_semibolditalic, FontWeight.SemiBold),
        Font(R.font.lora_bold, FontWeight.Bold),
        Font(R.font.lora_bolditalic, FontWeight.Bold),
        Font(R.font.lora_italic, FontWeight.Light)
    )
}








