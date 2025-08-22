package com.example.quanlybandienthoai.worker

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.CartRepository
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class SyncFirebaseToApiWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            if (!isNetworkAvailable(applicationContext)) {
                Log.e("SyncWorker", "Network not available, retrying...")
                return@withContext Result.retry()
            }

            val userId = inputData.getString("userId")
                ?: return@withContext Result.failure()

            val cart = CartRepository.getAllCarts(userId.toInt())
            val users = UserRepository.getUserById(userId.toInt())

            Log.e("SyncWorker", userId.toString())
            Log.e("SyncWorker", users.toString())
            Log.e("SyncWorker", cart.toString())


            // Đồng bộ dữ liệu
            val cartSyncJob = async {
                if (cart!=null) {
                    syncCartsWithApi(cart)
                }
            }
            val userSyncJob = async {
                if (users != null) {
                    syncUsersWithApi(users)
                }
            }

            // Chờ tất cả công việc hoàn thành
            awaitAll(userSyncJob, cartSyncJob)

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error during sync: ${e.message}", e)
            Result.retry()
        }
    }

    private suspend fun syncUsersWithApi(users: User) {
        try {
            val response = QuanlydienthoaiRepo.updateUserInSession(users)
        } catch (e: Exception) {
            Log.e("SyncWorker", "Exception while syncing user: ${e.message}", e)
        }
    }

    private suspend fun syncCartsWithApi(cart: Cart) {
        try {
            val response = QuanlydienthoaiRepo.updateCartInSession(cart)
        } catch (e: Exception) {
            Log.e("SyncWorker", "Exception while syncing cart: ${e.message}", e)
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
