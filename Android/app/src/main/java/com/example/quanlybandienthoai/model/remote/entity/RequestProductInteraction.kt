package com.example.quanlybandienthoai.model.remote.entity

import com.example.quanlybandienthoai.view.screens.admin.sanpham.ProductVariant

data class RequestProductInteraction(
    var name: String,
    var img: String,
    var xuatxu: Int,
    var hedieuhanh: Int,
    var thuonghieu: Int,
    var khuvuckho: Int,
    var giamgia: Int,
    var giare: Int,
    var dungluongpin: Int,
    var cameratruoc: String?,
    var camerasau: String?,
    var phienbanHDH: Int,
    var manhinh: String,
    var title: String,
    var description: String,
    var isTragop: Boolean,
    var listPB: List<ProductVariant>,
    var id: Int = -1,
)

data class khuvuckho(
    var id: Int,
    var tenKhuVuc: String
)

data class hedieuhanh(
    var id: Int,
    var tenHeDieuHanh: String
)

data class xuatxu(
    var id: Int,
    var tenXuatXu: String
)

data class ram(
    var id: Int,
    var kichThuocRam: Int
)

data class rom(
    var id: Int,
    var kichThuocRom: Int
)

data class color(
    var id: Int,
    var tenMauSac: String
)


