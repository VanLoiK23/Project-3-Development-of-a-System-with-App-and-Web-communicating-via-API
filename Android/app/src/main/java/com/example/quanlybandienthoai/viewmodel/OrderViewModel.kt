package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnotegiuaki.upload.UploadRepository
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.ResponseId
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.phieuxuat
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.model.repository.ReviewRepository
import com.example.quanlybandienthoai.worker.OrderStatusNotifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(val context: Application) : AndroidViewModel(context) {
    private val quanlydienthoaiApi = QuanlydienthoaiRepo

    private val reviewRepository = ReviewRepository

    private val productRepository = ProductRepository

    private val uploadRepository = UploadRepository()

    var userId by mutableStateOf<Int>(-1)

    var selectedTab by mutableStateOf<Int>(0)

    var orders by mutableStateOf<List<phieuxuat>>(emptyList())

    var order by mutableStateOf<phieuxuat?>(null)

    private val _review = MutableStateFlow(Review())
    val review: StateFlow<Review> get() = _review

    var ordersByTab by mutableStateOf<List<phieuxuat>>(emptyList())

    var statusCount by mutableStateOf<List<Int>>(emptyList())

    var count1 by mutableStateOf<Int>(0)

    var count2 by mutableStateOf<Int>(0)

    var count3 by mutableStateOf<Int>(0)

    var count4 by mutableStateOf<Int>(0)

    var count5 by mutableStateOf<Int>(0)

    var isCancelSuccess = mutableStateOf(false)

    //comment
    var rate by mutableStateOf(0f)
    var reviewContent by mutableStateOf("")
    var img by mutableStateOf("")
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var idExist by mutableStateOf(-1)


    var rateErr by mutableStateOf(false)
    var reviewContentErr by mutableStateOf(false)
    var selectedImageUriErr by mutableStateOf(false)
    var messageError by mutableStateOf("")

    var isErr by mutableStateOf(false)

    fun clear() {
        orders = emptyList()
        order = null
        ordersByTab = emptyList()
        count1 = 0
        count2 = 0
        count3 = 0
        count4 = 0
        count5 = 0
        statusCount= emptyList()
    }

    fun getAllPhieuXuat() {
        init()
        viewModelScope.launch(Dispatchers.IO) {
            orders = quanlydienthoaiApi.getAllOrderByUserId(userId).sortedByDescending { it.id }
                .filter { it.listctpx.isNotEmpty() }

            Log.e("Retrofit", orders.toString())

            if (orders.isNotEmpty()) {
                orders.forEach { order ->
                    if (order.listctpx.isNotEmpty()) {

                        if (order.status == 0) {
                            count1++
                        } else if (order.status == 1 || order.status == 2) {
                            count2++
                        } else if (order.status == 3) {
                            count3++
                        } else if (order.status == 4) {
                            count4++
                        } else {
                            count5++
                        }
                    }
                }

                statusCount = listOf(count1, count2, count3, count4, count5)

                getDetailOrders()
            }
        }
    }

    fun getDetailOrders() {
        when (selectedTab) {
            0 -> {
                ordersByTab = orders.filter { it.status == 0 }
            }

            1 -> {
                ordersByTab = orders.filter { it.status == 1 || it.status == 2 }
            }

            2 -> {
                ordersByTab = orders.filter { it.status == 3 }
            }

            3 -> {
                ordersByTab = orders.filter { it.status == 4 }
            }

            else -> {
                ordersByTab =
                    orders.filter { it.status != 0 && it.status != 1 && it.status != 2 && it.status != 3 && it.status != 4 }
            }
        }
    }

    fun getOrderDetails(id: Int) {
        order = orders.filter { it.id == id }[0]
    }

    fun cancel(reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            quanlydienthoaiApi.cancel(order?.id!!, reason)
            withContext(Dispatchers.Main) {
                order = order?.copy(
                    status = -2,
                    feeback = reason
                )
                isCancelSuccess.value = true
            }
        }
    }

    fun updateStatus(orderId: Int, status: Int) {
        order = orders.filter { it.id == orderId }[0].copy(
            status = status
        )
    }


    init {
        viewModelScope.launch {
            OrderStatusNotifier.orderStatusFlow.collect { (orderId, newStatus) ->
                getAllPhieuXuat()

                if (order?.id.toString() == orderId) {
                    // Cập nhật đơn hàng hiện tại (nếu có)
//                    order = order?.copy(status = newStatus.toInt())
                    updateStatus(orderId.toInt(), newStatus.toInt())
                }

                // Cập nhật đơn trong danh sách (nếu có)
//                val index = orders.indexOfFirst { it.id == orderId.toInt() }
//                if (index != -1) {
//                    val updatedOrder = orders[index].copy(status = newStatus.toInt())
//                    orders[index] = updatedOrder
//                }

            }
        }
    }

    fun validateReviewProduct(): Boolean {
        if (rate == 0f) {
            rateErr = true
            messageError = "Chưa chọn đánh giá sao"
        }
        if (reviewContent.isEmpty() || reviewContent.isBlank()) {
            reviewContentErr = true
            messageError = "Chưa nhập nội dung đánh giá"
        }
        if (selectedImageUri == null) {
            selectedImageUriErr = true
            messageError = "Chưa chọn ảnh đánh giá"
        }

        isErr = rateErr || reviewContentErr || selectedImageUriErr

        return isErr
    }

    var isUploading by mutableStateOf(false)
        private set

    fun uploadImage(context: Context, uri: Uri, nameImage: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            isUploading = true // Bắt đầu upload
            val result = uploadRepository.uploadImage(context, uri, nameImage)
            isUploading = false // Kết thúc upload
            if (result != null) {
                onSuccess(result) // Gọi callback khi upload xong
            }
        }
    }

    fun deleteImage(publicId: String) {
        viewModelScope.launch {
            uploadRepository.deleteImage(publicId)
        }
    }

    suspend fun reviewProduct(review: Review): ResponseId? {
        return withContext(Dispatchers.IO) {
            quanlydienthoaiApi.reviewProduct(review)
        }
    }


    fun updateReviewProduct(review: Review) {
        viewModelScope.launch(Dispatchers.IO) {
            quanlydienthoaiApi.updateReviewProduct(review)
        }
    }

    fun updateInfoReviewProduct(masp: Int, rate: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.updateRatingStats(masp, rate)
        }
    }

    suspend fun insertReviewFireBase(review: Review) {
        withContext(Dispatchers.IO) {
            reviewRepository.insertReview(review)
        }
    }

    var isReviewSucess = mutableStateOf(false)
    var tensp by mutableStateOf("")

    suspend fun getReviewExist(orderId: Int, ipSP: Int) {
        val review = reviewRepository.getReview(ipSP, orderId)

        if (review != null) {
            _review.value = review

            reviewContent = review.content
            rate = review.rate.toFloat()
            img = review.img
            idExist = review.id
        } else {
            idExist = -1
            _review.value = Review()
        }
    }

    var reviewMap by mutableStateOf(mapOf<Int, Boolean>())

    suspend fun isReviewExist(orderId: Int, productId: Int) {
        val review = reviewRepository.getReview(productId, orderId)
        reviewMap = reviewMap.toMutableMap().apply {
            put(productId, review != null)
        }
    }


    fun init() {
        count1 = 0
        count2 = 0
        count3 = 0
        count4 = 0
        count5 = 0
    }


}