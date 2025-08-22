package com.example.quanlybandienthoai.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.model.remote.entity.phieuxuat
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class OrderManageViewModel(val context: Application) : AndroidViewModel(context) {
    private val quanlydienthoaiApi = QuanlydienthoaiRepo

    private val _orders = MutableStateFlow<List<phieuxuat>>(emptyList())
    val orders: StateFlow<List<phieuxuat>> = _orders
    private val allOrder = MutableStateFlow<List<phieuxuat>>(emptyList())

    var order by mutableStateOf<phieuxuat?>(null)

    var isSuccess = mutableStateOf(false)

    var isWatchReason = mutableStateOf(false)

    var reason = mutableStateOf("")

    var isCancel = mutableStateOf(false)

    var idOrderCancel by mutableStateOf(0)

    var statusOrderCancel by mutableStateOf(0)

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val getOrders = quanlydienthoaiApi.getAllOrders()
            _orders.value = getOrders
            allOrder.value = getOrders
        }
    }

    fun setCurrentOrder(orderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            order = null
            val tempOrder = _orders.value.filter { it.id == orderId }[0]
            order = tempOrder
        }
    }

    fun updateOrderStatus(orderId: Int, status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            quanlydienthoaiApi.updateStatusOrder(orderId, status)
            _orders.update { list ->
                list.map { order ->
                    if (order.id == orderId) order.copy(status = status) else order
                }
            }
            isSuccess.value = true
        }
    }

    fun filterOrders(status: Int?, timeFilter: String) {
        val now = System.currentTimeMillis()
        val today = Calendar.getInstance()

        //	Tuyệt đối
        val weekStart = Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) }
        val monthStart = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }
        val yearStart = Calendar.getInstance().apply { set(Calendar.DAY_OF_YEAR, 1) }

        //Tương đối
        val todayLastYear = Calendar.getInstance().apply {
            add(Calendar.YEAR, -1) // Trừ đi 1 năm
        }


        val filtered = allOrder.value.filter { order ->
            val matchStatus = status == null || order.status == status
            val matchTime = when (timeFilter) {
                "today" -> {
                    val orderDate = order.date
                    val todayDate = today.time
                    orderDate!!.year == todayDate.year && orderDate.month == todayDate.month && orderDate.date == todayDate.date
                }

                "week" -> {
                    order.date!! >= Date(weekStart.timeInMillis)
                }

                "month" -> {
                    order.date!! >= Date(monthStart.timeInMillis)
                }

                else -> true
            }
            matchStatus && matchTime
        }
        _orders.value = filtered
    }


}