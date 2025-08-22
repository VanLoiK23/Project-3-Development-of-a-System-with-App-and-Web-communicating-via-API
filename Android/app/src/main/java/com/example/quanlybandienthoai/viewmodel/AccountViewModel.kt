package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.model.remote.api.QuanlydienthoaiApi
import com.example.quanlybandienthoai.model.repository.UserRepository
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AccountViewModel(val context: Application) : AndroidViewModel(context) {

    private val userRepository = UserRepository

    var userId by mutableStateOf("")
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var birthDate = mutableStateOf("")
    var phone by mutableStateOf("")


    var uNameError by mutableStateOf(false)
    var emailError by mutableStateOf(false)
    var dobError by mutableStateOf(false)
    var phoneError by mutableStateOf(false)

    private var error = false

    var openDialog = mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun validateManageFormInput(): Boolean {
        uNameError = username.isEmpty()
        phoneError = phone.isEmpty()
        emailError = email.isEmpty()
        dobError = birthDate.value.isEmpty()

        error = uNameError || phoneError || emailError || dobError
        return !error
    }

    var dbUser: User? = null

    fun initUser() = viewModelScope.launch {

        withContext(Dispatchers.IO) {
            dbUser = userRepository.getUserById(userId)

            if (dbUser != null) {
                username = dbUser!!.lastName + " " + dbUser!!.firstName
                phone = dbUser!!.phone
                birthDate.value = dbUser!!.ngaysinh
                email = dbUser!!.email
            }
        }

    }

    var isAdmin by mutableStateOf(false)


    fun updateUser() = viewModelScope.launch {

        val parts = username.split("\\s+".toRegex())

        val user = User(
            id = userId.toInt(),
            firstName = parts.last(),
            lastName = parts.dropLast(1).joinToString(" "),
            phone = phone,
            email = email,
            ngaysinh = birthDate.value,
            role = "khách hàng",
            status = "active"
        )

        withContext(Dispatchers.IO) {
            userRepository.insertUser(user)

            if(isAdmin){
                QuanlydienthoaiRepo.updateUserInSession(user)
            }
        }
        openDialog.value = true

    }


    fun reset() {
        phone = ""
        username = ""
        email = ""
        birthDate.value = ""
        errorMessage = ""
    }

    fun getFormattedBirthDate(): String {
        return try {
            val timestamp = birthDate.value.toLongOrNull()
            if (timestamp != null) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                sdf.format(Date(timestamp))
            } else {
                "Ngày không hợp lệ"
            }
        } catch (e: Exception) {
            Log.e("DateFormatError", "Lỗi khi xử lý ngày: ${e.message}")
            "Ngày không hợp lệ"
        }
    }

}

