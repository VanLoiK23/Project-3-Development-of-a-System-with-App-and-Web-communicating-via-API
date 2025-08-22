package com.thuongmaidientu.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.service.ICommentAndRateService;

@RestController(value = "CommentApiOfAdmin")
@RequestMapping("/quan-tri/danh-gia")
public class CommentAPI {
	@Autowired
	private ICommentAndRateService commentAndRateService;

	@DeleteMapping
	public ResponseEntity<String> deleteComment(@RequestParam("id") int id) {
		try {
			commentAndRateService.delete(id);

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping
	public ResponseEntity<?> getAllReviews() {
	    try {
	        List<CommentAndRateDTO> dtoList = commentAndRateService.selectAll();

	        return ResponseEntity.ok(dtoList); 
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy danh sách sản phẩm");
	    }
	}

}
