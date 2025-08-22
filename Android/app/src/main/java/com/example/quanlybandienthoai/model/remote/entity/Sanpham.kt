package com.example.quanlybandienthoai.model.remote.entity

import android.annotation.SuppressLint
import com.google.firebase.database.Exclude
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class Sanpham(
    var masp: Int,
    var tensp: String,
    var hinhanh: String,
    var xuatxu: String,
    var dungluongpin: Double,
    var manhinh: String,
    var hedieuhanh: String?,
    var phienbanhdh: Double?,
    var camerasau: String?,
    var cameratruoc: String?,
    var thuonghieu: Int,
    var khuvuckho: String,
    var soluongnhap: Int,
    var soluongban: Int,
    var promo: List<Map<String, Any>> = emptyList(),
    var sortDesc: String?,
    var detail: String?,
    var tongsao: Double,
    var soluongdanhgia: Int,
    var created: String?,
    var trash: String?,
    var status: Int?,

    @Exclude
    var pbspList: List<Phienbansanpham> = emptyList()
//    var reviews: MutableList<Review>
) {

    @SuppressLint("NewApi")
    constructor() : this(
        masp = -1,
        tensp = "",
        hinhanh = "",
        xuatxu = "",
        dungluongpin = -1.0,
        manhinh = "",
        hedieuhanh = "",
        phienbanhdh = -1.0,
        camerasau = "",
        cameratruoc = "",
        thuonghieu = -1,
        khuvuckho = "",
        soluongnhap = -1,
        soluongban = -1,
        promo = (emptyList()),
        sortDesc = "",
        detail = "",
        tongsao = -1.0,
        soluongdanhgia = -1,
        created = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        trash = "active",
        status = 1,
//    reviews = mutableListOf<Review>()
    )
}
