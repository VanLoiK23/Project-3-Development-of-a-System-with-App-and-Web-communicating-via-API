package com.example.quanlybandienthoai.model.remote.entity

data class EmailRequest(
    var toEmail:String,
    var orderItems:List<OrderItem>,
    var total:Int
)
