package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.quanlybandienthoai.model.remote.api.QuanlydienthoaiApi
import com.example.quanlybandienthoai.model.repository.UserRepository
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LoginViewModel(val context: Application) : AndroidViewModel(context) {
    private var currentUser: User? = null

    private val quanlydienthoaiApi = QuanlydienthoaiRepo

    private val userRepository = UserRepository


    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)

    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    private var error = false

    var isLoading by mutableStateOf(false)

    var errorMessage by mutableStateOf("")


    suspend fun validateLoginInput(): Boolean {
        if (email.isEmpty()) {
            emailError = true
            errorMessage = "Trường email rỗng không hợp lệ"
        } else if (password.isEmpty()) {
            passwordError = true
            errorMessage = "Trường password rỗng không hợp lệ"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = true
            errorMessage = "Trường email không đúng định dạng!!!"
        } else {
            // Kiểm tra email có bị khóa hay không
            val isLocked = userRepository.isEmailLocked(email)
            if (isLocked) {
                emailError = true
                errorMessage = "Tài khoản đã bị khóa. Vui lòng liên hệ hỗ trợ."
            }
        }

        error = emailError || passwordError
        return !error
    }

    suspend fun isExistInFireBase(): Boolean {

        if(userRepository.getUserByEmail(email) == null){
            errorMessage = "Tài khoản không tồn tại!!!."
        }

        return userRepository.getUserByEmail(email) != null
    }

    suspend fun authenticateLogin(): Boolean {
        isLoading = true
        return withContext(Dispatchers.IO) {
            val isSuccess = suspendCancellableCoroutine<Boolean> { continuation ->
                UserRepository.signInAuthentication(email, password) { onSuccess, message ->
                    if (message != null) {
                        errorMessage = message
                        isLoading = false
                    }
                    continuation.resume(onSuccess) // Trả kết quả vào suspend function
                }
            }

            if (isSuccess) {
                currentUser = UserRepository.getUserByEmail(email)
                isLoading = false
            }
            isSuccess
        }
    }

    suspend fun onLoginSuccess(userId: Int) {
        try {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
//                    return@addOnCompleteListener
//                }
//                val token = task.result
//                Log.d("FCM", "Token: $token")
//            }
            val token =
                FirebaseMessaging.getInstance().token.await() // Sử dụng await để chờ kết quả một cách đồng bộ
            Log.d("FCM_TOKEN", token)
            quanlydienthoaiApi.saveFCMToken(userId, token)
        } catch (e: Exception) {
            Log.e("FCM_TOKEN", "Error fetching token", e)
        }
    }


    fun getCurrentUserId(): String {
        return currentUser?.id.toString()
    }
}