package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Discount(
    var id: Int,
    var code: String,
    var discountAmount: Int,
    var numberUsed: Int,
    var expirationDate: Date,
    var paymentLimit: Int,
    var description: String
)
