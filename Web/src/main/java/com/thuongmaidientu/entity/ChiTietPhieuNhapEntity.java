package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitietphieunhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuNhapEntity {

	@EmbeddedId
	private ChiTietPhieuNhapKey id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE )
	@JoinColumn(name = "maphieunhap", insertable = false, updatable = false)
	private PhieuNhapEntity phieunhap;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maphienbansp", insertable = false, updatable = false)
	private PhienBanSanPhamEntity phienBanSanPhamNhap;

	@Column(name = "soluong", columnDefinition = "int DEFAULT 0")
	private Integer soLuong;

	@Column(name = "dongia", columnDefinition = "int DEFAULT 0")
	private Integer donGia;
}


