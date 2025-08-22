package com.thuongmaidientu.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.service.IArticleService;
import com.thuongmaidientu.service.ICommentArticleService;

@RestController(value = "ArticleApiOfAdmin")
@RequestMapping("/quan-tri/bai-viet")
public class ArticleAPI {
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private ICommentArticleService commentArticleService;

	@DeleteMapping
	public ResponseEntity<String> deleteBlog(@RequestParam("id") int id) {
		try {
			articleService.updateTrash(id, "disable");

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	
	@DeleteMapping("/deleteComment")
	public ResponseEntity<String> deleteComment(@RequestParam("idComment") int idComment) {
		try {
			commentArticleService.delete(idComment);

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}


}
