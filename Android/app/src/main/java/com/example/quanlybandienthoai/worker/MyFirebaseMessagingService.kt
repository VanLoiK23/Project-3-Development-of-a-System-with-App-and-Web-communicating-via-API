package com.example.quanlybandienthoai.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.viewmodel.OrderViewModel
import com.example.quanlybandienthoai.viewmodel.PlaceOrderViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        remoteMessage.data.let { data ->
            val orderId = data["order_id"] ?: return
            val newStatus = data["status"] ?: return

            CoroutineScope(Dispatchers.Default).launch {
                OrderStatusNotifier.notify(orderId, newStatus)
            }

        }

        // Hiện thông báo
        showNotification(title ?: "", body ?: "")
    }

    fun showNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Thông báo", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.icon_logo)
            .build()

        notificationManager.notify(0, notification)

    }
}

object OrderStatusNotifier {
    private val _orderStatusFlow = MutableSharedFlow<Pair<String, String>>(replay = 0)
    val orderStatusFlow = _orderStatusFlow.asSharedFlow()

    suspend fun notify(orderId: String, newStatus: String) {
        _orderStatusFlow.emit(orderId to newStatus)
    }
}

