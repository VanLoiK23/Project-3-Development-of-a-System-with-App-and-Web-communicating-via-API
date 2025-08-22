package com.thuongmaidientu.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sanpham")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "masp")
	private Integer id;

	@Column(name = "tensp", length = 255)
	private String tenSanPham;

	@Column(name = "alias", length = 255, nullable = false)
	private String alias;

	@Column(name = "hinhanh", length = 500)
	private String hinhAnh;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "xuatxu")
	private XuatXuEntity xuatXu;

	@Column(name = "dungluongpin")
	private Integer dungLuongPin;

	@Column(name = "manhinh", length = 255)
	private String manHinh;

	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hedieuhanh")
	private HedieuhanhEntity heDieuHanh;

	@Column(name = "phienbanhdh")
	private Double phienBanHDH;

	@Column(name = "camerasau", length = 255)
	private String cameraSau;

	@Column(name = "cameratruoc", length = 255)
	private String cameraTruoc;

	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "thuonghieu")
	private CategoryEntity thuongHieu;

	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "khuvuckho")
	private KhuVucKhoEntity khuVucKho;

	@Column(name = "soluongnhap")
	private Integer soLuongNhap = 0;

	@Column(name = "soluongban")
	private Integer soLuongBan = 0;

	@Column(name = "promo", columnDefinition = "LONGTEXT")
	private String promo;

	@Column(name = "sortDesc", length = 10000, nullable = false)
	private String sortDesc;

	@Column(name = "detail", columnDefinition = "LONGTEXT", nullable = false)
	private String detail;

	@Column(name = "tongsao")
	private Double tongSao = 0.0;

	@Column(name = "soluongdanhgia")
	private Integer soLuongDanhGia = 0;

	@Column(name = "created", nullable = false,updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name = "created_by", length = 100, nullable = false,updatable = false)
	private String createdBy;

	@Column(name = "modified", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@Column(name = "modified_by", length = 100, nullable = false)
	private String modifiedBy;

	@Column(name = "trash", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'active'")
	private String trash="active";

	@Column(name = "deleted_at")
	@Temporal(TemporalType.DATE)
	private Date deletedAt=null;

	@Column(name = "status", columnDefinition = "int(1) default 1")
	private Integer status=1;
	
	@OneToMany(mappedBy = "sanPham")
	private List<PhienBanSanPhamEntity> listPhienBanSP = new ArrayList<>();


	@OneToMany(mappedBy = "product")
	private List<CommentAndRateEntity> listcomment = new ArrayList<>();

	
	@Transient 
    private Integer xxID;
	
	@Transient 
    private Integer hdhID;
	
	@Transient 
    private Integer khoID;
	
	@Transient 
    private Integer thuonghieuID;
	
}
