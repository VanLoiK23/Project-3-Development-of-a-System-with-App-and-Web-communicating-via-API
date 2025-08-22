package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitietphieuxuat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuXuatEntity {

	@EmbeddedId
	private ChiTietPhieuXuatKey id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE )
	@JoinColumn(name = "maphieuxuat", insertable = false, updatable = false)
	private PhieuXuatEntity phieuXuat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maphienbansp", insertable = false, updatable = false)
	private PhienBanSanPhamEntity phienBanSanPhamXuat;

	@Column(name = "code_cart", length = 50, nullable = true)
	private String codeCart="";

	@Column(name = "soluong", columnDefinition = "int DEFAULT 0")
	private Integer soLuong;

	@Column(name = "dongia", columnDefinition = "int DEFAULT 0")
	private Integer donGia;
}

