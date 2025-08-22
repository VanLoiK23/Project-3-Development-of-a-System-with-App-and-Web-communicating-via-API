package com.example.quanlybandienthoai.model.remote.entity

data class ImportProduct(
    val ids: List<Int>,
    val prices: List<Int>,
    val quantity: List<Int>,
    val imei: List<Long>,
    val supplier: Long
)