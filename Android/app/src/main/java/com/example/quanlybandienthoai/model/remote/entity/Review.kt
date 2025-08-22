package com.example.quanlybandienthoai.model.remote.entity

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class Review(
    var id: Int = -1,
    var content: String,
    var rate: Double,
    var img: String,
    var id_user: Int,
    var id_sp: Int,
    var feeback: Int = 0,
    var feeback_content: String?=null,
    var nhanvien: String?=null,
    var ngayphanhoi: String?=null,
    var order_id: Int,
    var ngay_đanhgia: String,
    var user:String=""
) {
    @SuppressLint("NewApi")
    constructor() : this(
        id = -1,
        content = "",
        rate = 0.0,
        img = "",
        id_user = -1,
        id_sp = -1,
        feeback = 0,
        feeback_content = "",
        nhanvien = "",
        ngayphanhoi = "",
        order_id = -1,
        ngay_đanhgia =  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
}
