package com.thuongmaidientu.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuNhapKey implements Serializable { 
    @Column(name = "maphieunhap")
    private Integer maPhieuNhap;

    @Column(name = "maphienbansp")
    private Integer maPhienBanSP;
}
