package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "commentcomm_for_articles")
@Getter
@Setter
@NoArgsConstructor
public class CommentArticleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "content", nullable = false, length = 255)
	private String content;

	@Column(name = "rating", nullable = false)
	private Double rating;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private KhachHangEntity kh;

	@ManyToOne
	@JoinColumn(name = "id_articles", nullable = false)
	private ArticleEntity article;

	@Column(name = "feedback", nullable = false)
	private Integer feedback = 0;

	@Column(name = "content_feedback", length = 255, nullable = true)
	private String contentFeedback;

	@Column(name = "nhanvien", length = 255, nullable = true)
	private String nhanVien;

	@Column(name = "ngaydanhgia", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayDanhGia;

	@Column(name = "ngayphanhoi", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngayPhanHoi;
}
