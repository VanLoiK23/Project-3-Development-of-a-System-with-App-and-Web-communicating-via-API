package com.example.quanlybandienthoai.model.remote.entity

import java.io.Serializable

@kotlinx.serialization.Serializable
data class chitietphieuxuat(
    var phieuXuatId: Int,
    var phienBanSanPhamXuatId: Int,
    var codeCart: String? = null,
    var soLuong: Int,
    var donGia: Int,

    //order Info
    val maSP: Int? = 0,
    val tenSP: String? = null,
    val config: String? = null,
    val srcImage: String? = null

) : Serializable

