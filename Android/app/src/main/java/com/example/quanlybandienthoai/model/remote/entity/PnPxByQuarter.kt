package com.example.quanlybandienthoai.model.remote.entity

data class PnPxByQuarter(
    var phieuxuat: MutableMap<Int, Double> = mutableMapOf(),
    var phieunhap: MutableMap<Int, Double> = mutableMapOf()
) {
    init {
        // Khởi tạo 4 quarter với giá trị 0
        for (i in 1..4) {
            phieuxuat[i] = 0.0
            phieunhap[i] = 0.0
        }
    }
}