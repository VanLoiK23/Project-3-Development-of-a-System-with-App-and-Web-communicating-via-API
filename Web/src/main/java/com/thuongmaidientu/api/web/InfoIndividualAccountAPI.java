package com.thuongmaidientu.api.web;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.dto.PhieuXuatDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.ThongTinGiaoHangDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.CloudinaryService;
import com.thuongmaidientu.service.ICommentAndRateService;
import com.thuongmaidientu.service.IPhieuXuatService;
import com.thuongmaidientu.service.IProductService;
import com.thuongmaidientu.service.IThongTinGiaoHangService;

import jakarta.servlet.http.HttpSession;

@RestController("InfoIndividualOfWebAPI")
@RequestMapping("/Info-Individual")
public class InfoIndividualAccountAPI {

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private ICommentAndRateService commentAndRateService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IPhieuXuatService phieuXuatService;

	@Autowired
	private IThongTinGiaoHangService thongTinGiaoHangService;

	@PostMapping("/addComment")
	public ResponseEntity<?> upsertComment(@RequestParam(value = "rating", required = false) Double rating,
			@RequestParam("masp") Integer masp, @RequestParam("maphieuxuat") Integer orderId,
			@RequestParam(value = "review", required = false) String review,
			@RequestParam(value = "image", required = false) MultipartFile img, HttpSession session) {
		try {
			UserDTO userDTO = (UserDTO) session.getAttribute("user");

			CommentAndRateDTO commentAndRateDTO = commentAndRateService.getExistCommentByMaspAndOrderId(masp, orderId,
					userDTO.getId().intValue());

			if (commentAndRateDTO == null) {
				commentAndRateDTO = new CommentAndRateDTO();

				commentAndRateDTO.setNgayDanhGia(new Date());
				commentAndRateDTO.setFeeback(0);
			}

			if (img != null && !img.isEmpty()) {
				String randomName = "img_" + System.currentTimeMillis();
				commentAndRateDTO.setImg(cloudinaryService.uploadFile(img, randomName));
			}

			if (rating != null) {
				commentAndRateDTO.setRate(rating);
			}
			if (review != null && !review.isEmpty()) {
				commentAndRateDTO.setContent(review);
			}
			/*
			 * commentAndRateDTO.setRate(rating); commentAndRateDTO.setContent(review);
			 */
			commentAndRateDTO.setId_sp(masp);
			commentAndRateDTO.setOrder_id(orderId);
			commentAndRateDTO.setId_user(userDTO.getId().intValue());

			CommentAndRateDTO saveCommentAndRateDTO = commentAndRateService.save(commentAndRateDTO);

			syncProductRatings();

			return ResponseEntity.ok(Map.of("success", saveCommentAndRateDTO != null));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body("Lỗi xử lý bình luận");
		}
	}

	@GetMapping("/show_comment")
	public ResponseEntity<?> showComment(@RequestParam("masp") Integer masp,
			@RequestParam("maphieuxuat") Integer orderId, HttpSession session) {
		try {
			UserDTO userDTO = (UserDTO) session.getAttribute("user");
			CommentAndRateDTO commentAndRateDTO = commentAndRateService.getExistCommentByMaspAndOrderId(masp, orderId,
					userDTO.getId().intValue());

			return ResponseEntity.ok((Map.of("rating", commentAndRateDTO.getRate(), "reviewText",
					commentAndRateDTO.getContent(), "imageUrl", commentAndRateDTO.getImg())));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body("Lỗi xử lý bình luận");
		}
	}

	@PostMapping("/updateNote")
	public ResponseEntity<?> updateNoteShippingInfo(@RequestParam("id_shipping") Integer id_shipping,
			@RequestParam("note") String note, HttpSession session) {
		try {
			ThongTinGiaoHangDTO thongTinGiaoHangDTO = thongTinGiaoHangService.findById(id_shipping);

			thongTinGiaoHangDTO.setNote(note);

			ThongTinGiaoHangDTO saveGiaoHangDTO = thongTinGiaoHangService.insertAddress(thongTinGiaoHangDTO);

			return ResponseEntity.ok((Map.of("success", saveGiaoHangDTO != null)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body("Lỗi xử lý bình luận");
		}
	}

	@PostMapping("/filterOrder")
	public ResponseEntity<?> filterOrderByDate(@RequestParam("filter") String timeFilter, HttpSession session) {
		try {
			UserDTO userDTO = (UserDTO) session.getAttribute("user");
			if (userDTO == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập.");
			}

			List<PhieuXuatDTO> list;

			if ("all".equals(timeFilter)) {
				list = phieuXuatService.findAll(Pageable.unpaged());
			} else {
				LocalDate fromDate = null;
				switch (timeFilter) {
				case "today":
					fromDate = LocalDate.now();
					break;
				case "yesterday":
					fromDate = LocalDate.now().minusDays(1);
					break;
				case "week":
					fromDate = LocalDate.now().with(DayOfWeek.MONDAY);
					break;
				case "month":
					fromDate = LocalDate.now().withDayOfMonth(1);
					break;
				case "year":
					fromDate = LocalDate.now().withDayOfYear(1);
					break;
				default:
					return ResponseEntity.badRequest().body("Tham số filter không hợp lệ.");
				}

				list = phieuXuatService.findAllByDateAndStatus(java.sql.Date.valueOf(fromDate), null,
						Pageable.unpaged());
			}

			List<PhieuXuatDTO> filteredList = list.stream()
					.filter(px -> px.getMakh() == userDTO.getId().intValue() && px.getStatus() != 4)
					.collect(Collectors.toList());

			return ResponseEntity.ok(filteredList);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body("Lỗi xử lý đơn hàng");
		}
	}

	public void syncProductRatings() {
		List<ProductDTO> allProducts = productService.getALlProducts();

		for (ProductDTO product : allProducts) {
			List<CommentAndRateDTO> ratings = commentAndRateService.findByProductId(product.getId().intValue());

			int soLuongDanhGia = ratings.size();
			double tongSao = ratings.stream().mapToDouble(CommentAndRateDTO::getRate).sum();

			product.setSoLuongDanhGia(soLuongDanhGia);
			product.setTongSao(tongSao);
			productService.updateReviewProductAll(soLuongDanhGia, tongSao, product.getId());
		}
	}
}
