package com.example.quanlybandienthoai.model.remote.entity

data class PurchaseRequest(
    val momo: momo,
    val donhang: phieuxuat,
    val chitietphieuxuatList: List<chitietphieuxuat>
)

