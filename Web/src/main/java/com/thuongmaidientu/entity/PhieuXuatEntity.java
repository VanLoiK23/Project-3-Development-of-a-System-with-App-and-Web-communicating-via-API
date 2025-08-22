package com.thuongmaidientu.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phieuxuat")
@Getter
@Setter
@NoArgsConstructor
public class PhieuXuatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "maphieuxuat")
	private Integer id;

	@Column(name = "thoigian", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date thoiGian;

	@Column(name = "tongtien")
	private BigDecimal tongTien;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "makh")
	private KhachHangEntity khachHangMua; // Khóa ngoại đến bảng khách hàng

	@Column(name = "code_cart", nullable = true, length = 50)
	private String codeCart="";

	@Column(name = "status", nullable = false)
	private Integer status=0;

	@Column(name = "payment", nullable = true, length = 50)
	private String payment="";

	@Column(name = "cart_shipping", nullable = false)
	private Integer cartShipping;

	@Column(name = "discount_code",nullable = true)
	private Integer discountCode;

	@Column(name = "save", length = 10, nullable = false)
	private String save = "active";

	@Column(name = "fee_transport", nullable = false)
	private Integer feeTransport = 0;

	@Column(name = "feeback", length = 255, nullable = true)
	private String feeback;

	@Column(name = "updated_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@OneToMany(mappedBy = "phieuXuat")
	private List<ChiTietPhieuXuatEntity> phieuxuatList = new ArrayList<>();

	@OneToMany(mappedBy = "px")
	private List<CTSanPhamEntity> phieunhapCTSP = new ArrayList<>();
	
	@OneToMany(mappedBy = "order")
	private List<CommentAndRateEntity> listComment = new ArrayList<>();
}
