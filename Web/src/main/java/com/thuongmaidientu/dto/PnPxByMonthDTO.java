package com.thuongmaidientu.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class PnPxByMonthDTO {
	private Map<Integer, Double> phieuxuat;
    private Map<Integer, Double> phieunhap;

    public PnPxByMonthDTO() {
        // Khởi tạo 12 tháng với giá trị 0
        phieuxuat = new HashMap<>();
        phieunhap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            phieuxuat.put(i, 0.0);
            phieunhap.put(i, 0.0);
        }
    }
}
