package com.example.quanlybandienthoai.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.example.quanlybandienthoai.model.remote.entity.PnPxByMonth
import com.example.quanlybandienthoai.model.remote.entity.PnPxByQuarter
import com.example.quanlybandienthoai.model.remote.entity.RevenueProductTopSelling
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeManageViewModel(val context: Application) : AndroidViewModel(context) {
    private val quanlydienthoaiApi = QuanlydienthoaiRepo

    private val _revenueOrders = MutableStateFlow<List<Any>>(emptyList())
    val revenueOrders: MutableStateFlow<List<Any>> = _revenueOrders

    private val _revenuePurchases = MutableStateFlow<List<Any>>(emptyList())
    val revenuePurchases: MutableStateFlow<List<Any>> = _revenuePurchases

    private val _revenueProductTops = MutableStateFlow<List<RevenueProductTopSelling>>(emptyList())
    val revenueProductTops: MutableStateFlow<List<RevenueProductTopSelling>> = _revenueProductTops

    private val _revenuePnPxMonth = MutableStateFlow<PnPxByMonth?>(null)
    val revenuePnPxMonth: MutableStateFlow<PnPxByMonth?> = _revenuePnPxMonth

    private val _revenuePnPxQuarter = MutableStateFlow<PnPxByQuarter?>(null)
    val revenuePnPxQuarter: MutableStateFlow<PnPxByQuarter?> = _revenuePnPxQuarter

    var quantityWareHouse by mutableStateOf(0)

    var quantityProduct by mutableStateOf(0)

    var quantityUser by mutableStateOf(0)

    var quantityPurchase by mutableStateOf(0)

    var order by mutableStateOf(null)


    fun getInfoGeneral() {
        viewModelScope.launch(Dispatchers.IO) {
            val infoGeneral = quanlydienthoaiApi.getInfoGeneral()
            if (infoGeneral.isNotNull()) {
                quantityUser = infoGeneral!!.quantityUser
                quantityWareHouse = infoGeneral.quantityWareHouse
                quantityProduct = infoGeneral.quantityProduct
                quantityPurchase = infoGeneral.quantityPurchase
            }
        }
    }

    fun getInfoRevenueOrder(timeFilter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (timeFilter == "week") {
                val revenueOrder = quanlydienthoaiApi.getOrderFollowWeek()
                if (revenueOrder != null) {
                    _revenueOrders.value = revenueOrder
                }
            } else {
                val revenueOrder = quanlydienthoaiApi.getOrderFollowMonth()
                if (revenueOrder != null) {
                    _revenueOrders.value = revenueOrder
                }
            }
        }
    }

    fun getInfoRevenuePurchase(timeFilter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (timeFilter == "week") {
                val revenuePurchase = quanlydienthoaiApi.getPurchaseFollowWeek()
                if (revenuePurchase != null) {
                    _revenuePurchases.value = revenuePurchase
                }
            } else {
                val revenuePurchase = quanlydienthoaiApi.getPurchaseFollowMonth()
                if (revenuePurchase != null) {
                    _revenuePurchases.value = revenuePurchase
                }
            }
        }
    }

    fun getInfoProductTopSelling() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = quanlydienthoaiApi.getProductTopSelling()
            if (products != null) _revenueProductTops.value = products
        }
    }

    fun getInfoPnPxFollowYear() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = quanlydienthoaiApi.getPnPxFollowYear()
            if (data != null) {
                _revenuePnPxMonth.value = data
            }
        }
    }

    fun getInfoPnPxFollowQuarter() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = quanlydienthoaiApi.getPnPxFollowQuarter()
            if (data != null) {
                _revenuePnPxQuarter.value = data
            }
        }
    }

}