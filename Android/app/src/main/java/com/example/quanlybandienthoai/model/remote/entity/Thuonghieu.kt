package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Thuonghieu(
    var tenthuonghieu: String,
    var image: String,
    var mathuonghieu: Int = -1,
    var status: Int = 1,
    var trash: String = "active",

    ) {
    constructor() : this(
        tenthuonghieu = "",
        image = "",
        mathuonghieu = -1,
        status = 1,
        trash = "active"
    )
}