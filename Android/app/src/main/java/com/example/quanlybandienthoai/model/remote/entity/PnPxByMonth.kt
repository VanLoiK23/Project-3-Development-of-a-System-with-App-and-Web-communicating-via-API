package com.example.quanlybandienthoai.model.remote.entity

data class PnPxByMonth(
    var phieuxuat: MutableMap<Int, Double> = mutableMapOf(),
    var phieunhap: MutableMap<Int, Double> = mutableMapOf()
) {
    init {
        // Khởi tạo 12 tháng với giá trị 0
        for (i in 1..12) {
            phieuxuat[i] = 0.0
            phieunhap[i] = 0.0
        }
    }
}