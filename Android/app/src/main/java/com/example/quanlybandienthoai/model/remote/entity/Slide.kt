package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Slide(
    var image: String,
    var maSlide: Int = -1,
    var status: Int = 1,
    var trash: String = "active"
    ) {
    constructor() : this(
        image = "",
        maSlide = -1,
        status = 1,
        trash = "active"
    )
}