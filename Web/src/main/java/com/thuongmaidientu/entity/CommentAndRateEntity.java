package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "comment_and_rate")
@Getter
@Setter
@NoArgsConstructor
public class CommentAndRateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "content", nullable = false, length = 255)
	private String content;

	@Column(name = "rate", nullable = false)
	private Double rate;

	@Column(name = "img", nullable = false, length = 100)
	private String img;

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	private KhachHangEntity user;

	@ManyToOne
	@JoinColumn(name = "id_sp", nullable = false)
	private ProductEntity product;

	@Column(name = "feeback", nullable = true)
	private Integer feedback = 0;

	@Column(name = "feeback_content", length = 255, nullable = true)
	private String feedbackContent;

	@Column(name = "nhanvien", length = 255, nullable = true)
	private String nhanVien;

	@Column(name = "ngayphanhoi", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayPhanHoi;
	
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = true)
	private PhieuXuatEntity order;

	@Column(name = "ngay_đanhgia", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date ngayDanhGia;
}
