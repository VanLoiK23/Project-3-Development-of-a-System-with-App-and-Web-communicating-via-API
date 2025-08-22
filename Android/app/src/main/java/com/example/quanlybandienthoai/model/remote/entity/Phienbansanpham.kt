package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class Phienbansanpham(
    var masp: Int=-1,
    var maphienbansp: Int ,
    var rom: String?,
    var ram: String?,
    var mausac: String,
    var gianhap: Double,
    var giaxuat: Double,
    var soluongton: Int,
    var sale: Float,
    var price_sale: Double,
) {
    constructor() : this(
        masp = -1,
        maphienbansp = -1,
        rom = "",
        ram = "",
        mausac = "",
        gianhap = -1.0,
        giaxuat = -1.0,
        soluongton = -1,
        sale = 0.0f,
        price_sale = 0.0
    )
}
