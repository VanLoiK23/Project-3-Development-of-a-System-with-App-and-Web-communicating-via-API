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
public class ChiTietPhieuXuatKey implements Serializable { 
	@Column(name = "maphieuxuat")
	private Integer maphieuxuat;

	@Column(name = "maphienbansp")
	private Integer maPhienBanSP;
}
