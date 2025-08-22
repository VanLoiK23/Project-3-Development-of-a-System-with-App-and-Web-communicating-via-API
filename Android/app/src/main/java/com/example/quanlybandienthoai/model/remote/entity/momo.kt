package com.example.quanlybandienthoai.model.remote.entity

data class momo(
    val partnerCode: String,
    val orderId: Long,
    val amount: String,
    val orderInfo: String,
    val orderType: String,
    val transId: Long,
    val payType: String,
    val codeCart: Int
//    val token:String
)
