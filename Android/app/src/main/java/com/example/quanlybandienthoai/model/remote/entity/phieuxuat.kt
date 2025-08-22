package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.Date

@Serializable
data class phieuxuat(
    var id: Int = -1,
    var tongTien: BigDecimal,
    var makh: Int,
    var codeCart: String?,
    var status: Int,
    var payment: String?,
    var cartShipping: Int,
    var discountCode: Int?,
//    var save: String,
    var feeTransport: Int,
    var feeback: String?,
    var date: Date? = Date(),

    //just display in order
    var infoOrderDiscount: Discount? = null,
    var listctpx: List<chitietphieuxuat> = emptyList(),
    var name: String? = null,
    var phone: String? = null,
    var address: String? = null,
)
