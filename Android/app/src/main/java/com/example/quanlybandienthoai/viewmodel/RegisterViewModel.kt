package com.example.quanlybandienthoai.viewmodel

import android.app.Application
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


class RegisterViewModel(val context: Application) : AndroidViewModel(context) {

    private val userRepository = UserRepository
    private val quanlydienthoaiApi = QuanlydienthoaiRepo

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var birthDate = mutableStateOf("")

    var fNameError by mutableStateOf(false)
    var lNameError by mutableStateOf(false)
    var emailError by mutableStateOf(false)
    var phoneError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var dobError by mutableStateOf(false)
    private var error = false

    var isLoading by mutableStateOf(false)
    var openDialog = mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var isSuccess by mutableStateOf(false)

    fun validateRegisterInput(): Boolean {
        fNameError = firstName.isEmpty()
        lNameError = lastName.isEmpty()
        emailError = email.isEmpty()
        phoneError = phone.isEmpty()
        passwordError = password.isEmpty()
        dobError = birthDate.value.isEmpty()

        error = fNameError || lNameError || emailError || passwordError || dobError
        return !error
    }

    fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private var dbUser: User? = null

    suspend fun validateExistingAccount(): Boolean {
        return withContext(Dispatchers.IO) {
            dbUser = userRepository.getUserByEmail(email)
            dbUser == null
        }
    }

    suspend fun validateEmailLock(): Boolean {
        return withContext(Dispatchers.IO) {
            userRepository.isEmailLocked(email)
        }
    }

//    fun addUser() = viewModelScope.launch {
//        isLoading = true
//
//        val user = User(
//            firstName = firstName,
//            lastName = lastName,
//            phone = phone,
//            email = email,
//            ngaysinh = birthDate.value,
//            role = "khách hàng",
//            status = "active",
//            password = password
//        )
//
//        withContext(Dispatchers.IO) {
//
////            if (userData != null) {
////                userRepository.registerAuthentication(email, password) { onSuccess, message ->
////                    if (!onSuccess) {
////                        errorMessage = message ?: "Lỗi đăng ký"
////                    } else {
////                        userRepository.insertUser(user.copy(id = userData.id))
////                        reset()
////                        openDialog.value = true
////                        isSuccess = true
////                    }
////                    isLoading = false
////                }
//
//            userRepository.registerAuthentication(email, password) { onSuccess, uid, message ->
//                if (!onSuccess) {
//                    errorMessage = message ?: "Lỗi đăng ký"
//                } else {
//
//                    viewModelScope.launch {
//                        val userData = quanlydienthoaiApi.addUser(user)
//
//                        if (userData != null) {
//                            // Lưu người dùng với uid từ Firebase
//                            userRepository.insertUser(
//                                user.copy(
//                                    firebaseUid = uid,
//                                    id = userData.id
//                                )
//                            ) // nếu `id` là String
//                            reset()
//                            openDialog.value = true
//                            isSuccess = true
//                        } else {
//                            errorMessage = "Lỗi đăng ký"
//                            isLoading = false
//                        }
//                    }
//                }
//                isLoading = false
//            }
//
////            } else {
////                errorMessage = "Lỗi đăng ký"
////
////                isLoading = false
////            }
//        }
//
//    }

    fun addUser() = viewModelScope.launch {
        isLoading = true

        val user = User(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            ngaysinh = birthDate.value,
            role = "khách hàng",
            status = "active",
            password = password
        )

        try {
            val uidResult = withContext(Dispatchers.IO) {
                userRepository.registerAuthentication(email, password)
            }

            uidResult.fold(onSuccess = { uid ->
                viewModelScope.launch {
                    val userData = withContext(Dispatchers.IO) {
                        quanlydienthoaiApi.addUser(
                            user.copy(
                                firebaseUid = uid
                            )
                        )
                    }

                    if (userData != null) {
                        userRepository.insertUser(
                            user.copy(
                                id = userData.id,
                                firebaseUid = uid
                            )
                        )
                        reset()
                        openDialog.value = true
                        isSuccess = true
                    } else {
                        errorMessage = "Lỗi đăng ký người dùng"
                    }

                    isLoading = false
                }
            }, onFailure = { e ->
                errorMessage = e.message ?: "Lỗi đăng ký"
                isLoading = false
            })

        } catch (e: Exception) {
            errorMessage = e.message ?: "Đã xảy ra lỗi"
            isLoading = false
        }
    }


    fun reset() {
        firstName = ""
        lastName = ""
        email = ""
        password = ""
        birthDate.value = ""
        errorMessage = ""
        isSuccess = false
        isLoading = false
    }
}

