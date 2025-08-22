package com.example.quanlybandienthoai

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quanlybandienthoai.view.navigation.NavGraph
import com.example.quanlybandienthoai.view.screens.admin.home.DashboardScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.AddProductScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.EditProductScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.ProductManagementScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.ProductSelectionScreen
import com.example.quanlybandienthoai.worker.SyncFirebaseToApiWorker
import com.example.quanlybandienthoai.worker.SyncMySQLToFirebaseWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Modifier.systemBarsPadding()

        setContent {
            val navController = rememberNavController()

            Scaffold(
//                topBar = {SimpleHeader("",navController)},
//                bottomBar = { BottomNavigationBar(navController, mainViewModel) } // Gọi BottomNavigationBar
            ) { paddingValues ->
                NavGraph(navController = navController, Modifier.padding(paddingValues))
            }

//            AddProductScreen()

//            ProductManagementScreen(navController)

//            CheckoutScreen()
//            HomeScreen(navController)

//            setupPeriodicSync()
//                 runImmediateSync()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

//        setupPeriodicSyncForOnDestroy()

        runImmediateSyncForOnDestroy()
        setupPeriodicSyncForOnDestroy()
    }

    private fun setupPeriodicSync() {
        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .setRequiresCharging(true) // chỉ khi đang sạc
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncMySQLToFirebaseWorker>(
            1, TimeUnit.MINUTES
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            "SyncMySQLToFirebase", // Tên định danh công việc
            ExistingPeriodicWorkPolicy.KEEP, // Nếu đã có thì giữ nguyên
            syncRequest
        )
    }

    private fun runImmediateSync() {
        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val immediateWork = OneTimeWorkRequestBuilder<SyncMySQLToFirebaseWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(immediateWork)
    }

    private fun setupPeriodicSyncForOnDestroy() {
        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .setRequiresCharging(true) // chỉ khi đang sạc
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncFirebaseToApiWorker>(
            1, TimeUnit.SECONDS
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            "SyncFirebaseToMySQL", // Tên định danh công việc
            ExistingPeriodicWorkPolicy.KEEP, // Nếu đã có thì giữ nguyên
            syncRequest
        )
    }

    private fun runImmediateSyncForOnDestroy() {
        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val immediateWork = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(immediateWork)
    }

}