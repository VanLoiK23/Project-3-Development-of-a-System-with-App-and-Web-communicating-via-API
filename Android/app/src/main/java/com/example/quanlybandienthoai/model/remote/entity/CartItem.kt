package com.example.quanlybandienthoai.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    var cart_item_id: Int = -1,
    var cart_id: Int,
    var maphienbansp: Int = -1,
    val masp: Int = -1,
    var soluong: Int,
) {
    constructor() : this(
        cart_item_id = -1,
        cart_id = -1,
        maphienbansp = -1,
        masp = -1,
        soluong = -1
    )
}

