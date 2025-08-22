package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.model.repository.UserRepository
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ForgotPasswordViewModel(val context: Application) : AndroidViewModel(context) {
    private val userRepository = UserRepository
    private var currentUser: User? = null
    var email by mutableStateOf("")
    var emailError by mutableStateOf(false)
    private var error = false

    var isLoading by mutableStateOf(false)

    var showForgotPasswordDialog by  mutableStateOf(false)

    var openDialog = mutableStateOf(false)

    var messageDialog by mutableStateOf("")


    var errorMessage by mutableStateOf("")

    fun validateForgotInput(): Boolean {
        return when {
            email.isEmpty() -> {
                emailError = true
                errorMessage = "Trường email rỗng không hợp lệ"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailError = true
                errorMessage = "Trường email không đúng định dạng!!!"
                false
            }
            else -> {
                emailError = false
                errorMessage = ""
                true
            }
        }
    }


    suspend fun getUserByEmail(email: String): User? {
        return withContext(Dispatchers.IO) {
            userRepository.getUserByEmail(email)
        }
    }


    fun sendPasswordResetEmail(email: String
//                               , onResult: (String) -> Unit
    ) {
//        runBlocking {
//            this.launch(Dispatchers.IO) {
////                userRepository.sendEmailResetPassword(email){
////                        onSuccess,messsage->
////                    if (messsage != null) {
////                        errorMessage=messsage
////                    }
////                    if(onSuccess){
////                        messageDialog="Đã gửi email reset password mới vào email $email vui lòng check!"
////                    }
////                }
//            }
//        }
//        return messageDialog

        val user = User(
            firstName = "",
            lastName = "",
            phone = "",
            email = email,
            ngaysinh = "",
            role = "",
            status = "",
        )

        viewModelScope.launch(Dispatchers.IO) {
            val result = QuanlydienthoaiRepo.sendEmailResetPassword(user)
            withContext(Dispatchers.Main) {
                val message = if (result == "Email khôi phục đã được gửi!") {
                    "Đã gửi email reset password mới vào email $email, vui lòng kiểm tra!"
                } else {
                    result
                }

//                onResult(message)
            }
        }

    }



    fun openGmailApp(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Chỉ định gửi email
            setPackage("com.google.android.gm") // Chỉ mở ứng dụng Gmail
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Gmail is not available on this device", Toast.LENGTH_SHORT).show()
        }
    }





}