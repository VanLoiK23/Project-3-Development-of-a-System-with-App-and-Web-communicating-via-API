package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "khachhang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "makh")
	private Integer maKhachHang;

	@Column(name = "hoten", length = 100, nullable = false)
	private String hoTen;

	@Column(name = "matkhau", length = 255, nullable = true)
	private String matKhau;

	@Column(name = "gioitinh", length = 15, nullable = true)
	private String gioiTinh;

	@Column(name = "ngaysinh", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date ngaySinh;

	@Column(name = "sdt", length = 50, nullable = false)
	private String soDienThoai;

	@Column(name = "diachi", length = 255, nullable = true)
	private String diaChi;

	@Column(name = "email", length = 255, nullable = false)
	private String email;

	@Column(name = "trangthai", nullable = false)
	private Integer trangThai = 1;

	@OneToMany(mappedBy = "khachHang")
	private List<CartEntity> clientListCart = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<CommentAndRateEntity> clientCommentsProduct = new ArrayList<>();

	@OneToMany(mappedBy = "kh")
	private List<CommentArticleEntity> clientCommentsArticle = new ArrayList<>();

	@OneToMany(mappedBy = "khachHangMua")
	private List<PhieuXuatEntity> clientOrder = new ArrayList<>();

	@OneToMany(mappedBy = "khachHangDat")
	private List<ThongTinGiaoHangEntity> clientInfoAddress = new ArrayList<>();
}
