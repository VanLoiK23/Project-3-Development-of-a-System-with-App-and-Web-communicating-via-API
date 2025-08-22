package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: Int = -1,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var email: String,
    var ngaysinh: String,
    var role: String,
    var status: String,
    var gender:String="",
    var password:String="",
    var firebaseUid:String?="",
) {
    constructor() : this(
        id = -1,
        firstName = "",
        lastName = "",
        phone="",
        email = "",
        ngaysinh = "",
        role = "khách hàng",
        status = "",
        gender="",
        password="",
        firebaseUid="",
    )
}