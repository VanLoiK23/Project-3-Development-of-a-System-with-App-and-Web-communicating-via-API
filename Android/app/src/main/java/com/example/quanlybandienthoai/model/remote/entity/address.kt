package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class address(
    var id: Int = -1,
    var idkh: Int,
    var hovaten: String,
    var email: String,
    var sodienthoai: String,
    var street_name: String,
    var district: String,
    var city: String,
    var country: String,
    var note: String
)
