package com.example.quanlybandienthoai.model.remote.entity

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import java.util.*
import java.text.SimpleDateFormat

@Serializable @SuppressLint("SimpleDateFormat")
data class Cart(
    var id: Int = -1,
    var idkh: Int,

    @SerialName("created_at")
    var created_at: String =SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
    @SerialName("updated_at")
    var updated_at: String=SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
    var status: String= "active",
    val cartItems: List<CartItem> = emptyList()
) {
    @SuppressLint("SimpleDateFormat")
    constructor() : this(
        id = -1,
        idkh = -1,
        created_at = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), // Default value
        updated_at = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), // Default value
        status = "active"
    )
}

