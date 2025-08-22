package com.thuongmaidientu.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class PnPxByQuarterDTO {
    private Map<Integer, Double> phieuxuat;
    private Map<Integer, Double> phieunhap;

    public PnPxByQuarterDTO() {
        this.phieuxuat = new HashMap<>();
        this.phieunhap = new HashMap<>();

        // Khởi tạo 4 quý với giá trị 0
        for (int i = 1; i <= 4; i++) {
            phieuxuat.put(i, 0.0);
            phieunhap.put(i, 0.0);
        }
    }
}
