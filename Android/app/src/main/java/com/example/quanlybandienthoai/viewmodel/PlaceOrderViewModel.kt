package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.example.quanlybandienthoai.model.remote.api.QuanlydienthoaiApi
import com.example.quanlybandienthoai.model.remote.entity.Discount
import com.example.quanlybandienthoai.model.remote.entity.EmailRequest
import com.example.quanlybandienthoai.model.remote.entity.address
import com.example.quanlybandienthoai.model.remote.entity.chitietphieuxuat
import com.example.quanlybandienthoai.model.remote.entity.momo
import com.example.quanlybandienthoai.model.remote.entity.phieuxuat
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.CartRepository
import com.example.quanlybandienthoai.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PlaceOrderViewModel(val context: Application) : AndroidViewModel(context) {
    private val quanlydienthoaiApi = QuanlydienthoaiRepo
    private val userRepository = UserRepository

    var isNotValidAddress = mutableStateOf(false)

    var isPaymentSuccess by mutableStateOf(false)

    var isUploadSuccess by mutableStateOf<Boolean?>(null)
        private set

    fun markPaymentSuccess() {
        isPaymentSuccess = true

        isUploadSuccess = false
    }

    var cart_shipping by mutableStateOf(0)

    var fee_transport by mutableStateOf(0)

    var discounts by mutableStateOf<List<Discount>>(emptyList())
        private set

    var ctpxList by mutableStateOf<List<chitietphieuxuat>>(emptyList())

    var selectedDiscount by mutableStateOf<Discount?>(null)

    fun getAllDiscount() {
        viewModelScope.launch {
            discounts = quanlydienthoaiApi.getAllDiscount()

            Log.d("Test", discounts.toString())
        }
    }

    fun setCartCompleted(idCart:Int) {
        viewModelScope.launch {
            quanlydienthoaiApi.setCartCompleted(idCart)
        }
    }

    fun sendEmail(idKh: Int, emailRequest: EmailRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserById(idKh)

            if (user != null) {
                Log.d("Test", user.email)
                val updatedRequest = emailRequest.copy(toEmail = "huynhvanloi956@gmail.com")
                quanlydienthoaiApi.sendEmailOrder(updatedRequest)
            }
        }
    }


    fun placeOrder(
        momo: momo,
        phieuxuat: phieuxuat,
        chitietphieuxuatList: List<chitietphieuxuat>,
        obBackUp: (Boolean) -> Unit
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val result = quanlydienthoaiApi.insertPhieuXuat(momo, phieuxuat, chitietphieuxuatList)
            withContext(Dispatchers.Main) {
                if (result == "Export Success") {
                    obBackUp(true)
                    markPaymentSuccess()

                    Log.d("isRun", result)

                }
            }

        }

    }
}
