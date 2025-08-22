package com.thuongmaidientu.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.CommentArticleDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.ICommentArticleService;

import jakarta.servlet.http.HttpSession;

@RestController("blogApiOfWeb")
@RequestMapping("/bai-viet")
public class BlogAPI {
	@Autowired
	private ICommentArticleService commentArticleService;

	@PostMapping
	public ResponseEntity<?> addComment(@RequestParam("rating") Double rating, @RequestParam("comment") String content,
			@RequestParam("id") Integer idBlog, HttpSession session) {
		try {
			UserDTO userDTO = (UserDTO) session.getAttribute("user");

			CommentArticleDTO commentArticleDTO = new CommentArticleDTO();
			commentArticleDTO.setRating(rating);
			commentArticleDTO.setContent(content);
			commentArticleDTO.setIdArticle(idBlog);
			commentArticleDTO.setFeedback(0);

			
			if(userDTO!=null) {
				commentArticleDTO.setIdClient(userDTO.getId().intValue());
			}
			
			CommentArticleDTO saveArticleDTO = commentArticleService.save(commentArticleDTO);
			
			saveArticleDTO.setNameClient(userDTO.getName());

			return ResponseEntity.ok(saveArticleDTO);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body("Lỗi xử lý bình luận");
		}
	}
}
