package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ctsanpham")
@Getter
@Setter
@NoArgsConstructor
public class CTSanPhamEntity {

	@Id
	@Column(name = "maimei", nullable = false)
	private Long maImei;

	@ManyToOne
	@JoinColumn(name = "maphienbansp", nullable = false)
	private PhienBanSanPhamEntity productVersion;

	@ManyToOne
	@JoinColumn(name = "maphieunhap", nullable = false)
	private PhieuNhapEntity pn;

	@ManyToOne
	@JoinColumn(name = "maphieuxuat", nullable = true)
	private PhieuXuatEntity px = null;

	@Column(name = "tinhtrang", nullable = true)
	private Integer tinhTrang = 0;
}
