package com.example.quanlybandienthoai.worker

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.Slide
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.BrandRepository
import com.example.quanlybandienthoai.model.repository.CartRepository
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.model.repository.ReviewRepository
import com.example.quanlybandienthoai.model.repository.SlideRepository
import com.example.quanlybandienthoai.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class SyncMySQLToFirebaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

//    override suspend fun doWork(): Result {
//        return try {
//            if (!isNetworkAvailable(applicationContext)) {
//                return Result.retry()
//            }
//
//            val products = QuanlydienthoaiRepo.getProducts()
//            Log.d("AAA", products.toString())
//
//            val users = QuanlydienthoaiRepo.getAllUser()
//            Log.d("AAA", users.toString())
//
////            val sampleSanphamList = listOf(
////                Sanpham(
////                    masp = 1,
////                    tensp = "Samsung Galaxy S25 Ultra",
////                    hinhanh = "https://img.upanh.tv/2025/03/21/download.jpg",
////                    xuatxu = 1, // 1: Việt Nam, 2: Hàn Quốc, v.v.
////                    dungluongpin = 5000.0,
////                    manhinh = "6.8 inch Dynamic AMOLED 2X",
////                    hedieuhanh = 1, // 1: Android, 2: iOS, v.v.
////                    phienbanhdh = 14.0,
////                    camerasau = "200MP + 10MP + 10MP + 12MP",
////                    cameratruoc = "12MP",
////                    thuonghieu = 1, // 1: Samsung, 2: Apple, v.v.
////                    khuvuckho = "", // 3: Hà Nội
////                    soluongnhap = 100,
////                    soluongban = 20,
////                    promo = listOf(
////                        mapOf("name" to "giamgia", "value" to 10),
////                    ),promo=[{name=giareonline, value=10}, {name=giamgia, value=4}]
////                    sortDesc = "Siêu phẩm mới nhất từ Samsung",
////                    detail = "Điện thoại flagship với chip Snapdragon 8 Gen 3, bút S-Pen và camera 200MP.",
////                    tongsao = 1200,
////                    soluongdanhgia = 400,
////                    created = "2024-03-20",
////                    trash = "0",
////                    status = 1,
////                    listOf(
////                        Phienbansanpham(
////                            masp = 1,
////                            maphienbansp = 101,
////                            rom = 256,
////                            ram = 12,
////                            mausac = "", // 1: Đen
////                            gianhap = 25000000.0,
////                            giaxuat = 29990000.0,
////                            soluongton = 50,
////                            sale = 0.0, // Giảm 10%
////                            price_sale = 29990000.0
////                        ),
////                        Phienbansanpham(
////                            masp = 1,
////                            maphienbansp = 102,
////                            rom = 512,
////                            ram = 12,
////                            mausac = "", // 2: Xanh
////                            gianhap = 27000000.0,
////                            giaxuat = 32990000.0,
////                            soluongton = 30,
////                            sale = 0.0, // Giảm 15%
////                            price_sale = 32990000.0
////
////                        )
////                    )
////                )
////            )
////
////            homeViewModel.insertSanpham(sampleSanphamList[0], ssModel)
////            homeViewModel.insertSanpham(sampleSanphamList[1], ipModel)
////            homeViewModel.insertSanpham(sampleSanphamList[2], nokia)
////            homeViewModel.insertSanpham(sampleSanphamList[3], mobi)
////            homeViewModel.insertSanpham(sampleSanphamList[4], xx)
//
//            products.forEach { product ->
////                ProductRepository.insertProduct(product)
//                ProductRepository.insertSanphamWithPhienbans(product, product.pbspList)
//                Log.d("AAA", product.pbspList.toString())
//
//            }
//
//            users.forEach { user->
//                UserRepository.insertUser(user)
//            }
//
//            Result.success()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Result.retry()
//        }
//    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            if (!isNetworkAvailable(applicationContext)) {
                Log.e("SyncWorker", "Network not available, retrying...")
                return@withContext Result.retry()
            }

            val products = QuanlydienthoaiRepo.getProducts()
            val users = QuanlydienthoaiRepo.getAllUser()
//            val carts = QuanlydienthoaiRepo.getAllCarts()
            val slides = QuanlydienthoaiRepo.getAllSlides()
            val brands = QuanlydienthoaiRepo.getAllThuongHieus()
            val reviews = QuanlydienthoaiRepo.getAllReviews()

            // Đồng bộ dữ liệu
            val productSyncJobs = products.map { product ->
                async { syncProduct(product) }
            }
            val userSyncJobs = users.map { user ->
                async { syncUser(user) }
            }
//            val cartSyncJobs = carts.map { cart ->
//                async { syncCart(cart) }
//            }
            val slideSyncJobs = slides.map { slide ->
                async { syncSlide(slide) }
            }
            val brandSyncJobs = brands.map { brand ->
                async { syncThuongHieu(brand) }
            }
            val reviewSyncJobs = reviews.map { review ->
                async { syncReview(review) }
            }

            // Chờ tất cả công việc hoàn thành
            (productSyncJobs + userSyncJobs + slideSyncJobs + brandSyncJobs + reviewSyncJobs).awaitAll()

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error during sync: ${e.message}", e)
            Result.retry()
        }
    }

    private suspend fun syncProduct(product: Sanpham) {
        try {
            ProductRepository.insertSanphamWithPhienbans(product, product.pbspList)
            Log.d("SyncWorker", "Synced product: ${product.masp}")
        } catch (e: Exception) {
            Log.e("SyncWorker", "Failed to sync product: ${product.masp}", e)
        }
    }

    private suspend fun syncUser(user: User) {
        try {
            UserRepository.insertUser(user)
            Log.d("SyncWorker", "Synced user: $user")
        } catch (e: Exception) {
            Log.e("SyncWorker", "Failed to sync user: $user", e)
        }
    }

    private suspend fun syncSlide(slide: Slide) {
        try {
            SlideRepository.insertSlide(slide)
            Log.d("SyncWorker", "Synced slide: $slide")
        } catch (e: Exception) {
            Log.e("SyncWorker", "Failed to sync slide: $slide", e)
        }
    }

    private suspend fun syncThuongHieu(thuonghieu: Thuonghieu) {
        try {
            BrandRepository.insertCategory(thuonghieu)
            Log.d("SyncWorker", "Synced brand: $thuonghieu")
        } catch (e: Exception) {
            Log.e("SyncWorker", "Failed to sync brand: $thuonghieu", e)
        }
    }

    private suspend fun syncCart(cart: Cart) {
        try {
            CartRepository.insertCartSynchFromMySql(cart)

            cart.cartItems.forEach { cartitem ->
                CartRepository.insertCartItem(cart.id, cartitem)
            }

            Log.d("SyncWorker", "Synced cart: $cart")
        } catch (e: Exception) {
            Log.e("SyncWorker", "Failed to sync cart: $cart", e)
        }
    }

    private suspend fun syncReview(review: Review) {
        try {
            ReviewRepository.insertReview(review)
            Log.d("SyncWorker", "Synced review: $review")
        } catch (e: Exception) {
            Log.e("SyncWorker", "Failed to sync review: $review", e)
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

}
