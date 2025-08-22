package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.example.quanlybandienthoai.model.remote.api.QuanlydienthoaiApi
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.example.quanlybandienthoai.model.repository.*
import com.example.quanlybandienthoai.model.remote.entity.*
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.worker.SyncFirebaseToApiWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class AppViewModel(val context: Application) : AndroidViewModel(context) {
    private var userRepository = UserRepository
    private var productRepository = ProductRepository
    private var currentUserId: String = ""
    private var currentUser: User? = null
    var isAdmin = false

    var isLoginSuccess = mutableStateOf(false)


    fun setCurrentUserId(userId: String) {
        currentUserId = userId
        if (userId == "") return

        runBlocking {
            this.launch(Dispatchers.IO) {
                currentUser = userRepository.getUserById(userId)
            }
        }
        isAdmin = currentUser?.role == "nhân viên"
    }

    fun logOut() {
        currentUser = null
        currentUserId = ""
        isAdmin = false
    }

    fun getCurrentUserId(): String {
        return currentUserId
    }

    fun getCurrentUser(): User? {
        if (currentUserId == "") return null
        runBlocking {
            this.launch(Dispatchers.IO) {
                currentUser = userRepository.getUserById(currentUserId)
            }
        }
        return currentUser
    }


}