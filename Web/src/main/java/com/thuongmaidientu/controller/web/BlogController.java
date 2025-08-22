package com.thuongmaidientu.controller.web;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.thuongmaidientu.dto.ArticleDTO;
import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.dto.CommentArticleDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.entity.CommentArticleEntity;
import com.thuongmaidientu.service.IArticleService;
import com.thuongmaidientu.service.ICommentArticleService;

import jakarta.servlet.http.HttpSession;

@Controller(value = "blogOfWebController")
public class BlogController {
	@Autowired
	private IArticleService articleService;

	@Autowired
	private ICommentArticleService commentArticleService;

	@RequestMapping(value = { "/Blog", "/Blog/{id}" }, method = RequestMethod.GET)
	public ModelAndView Detail(@PathVariable(name = "id", required = false) Integer id, HttpSession session) {
		ModelAndView mav = new ModelAndView("web/blog/Blog");

		ArticleDTO articleDTO = new ArticleDTO();

		CommentArticleDTO commentArticleDTO = new CommentArticleDTO();

		if (id == null) {
			articleDTO.setListResult(articleService.selectAll());
		} else {
			articleDTO = articleService.findById(id);

			articleDTO.setListResult(articleService.selectAll().stream().filter(blog -> blog.getId().intValue() != id)
					.collect(Collectors.toList()));

			mav.addObject("isDetail", "true");

			mav.addObject("nextId", articleService.getNextId(id));

			mav.addObject("preId", articleService.getPreviousId(id));

			List<CommentArticleDTO> commentArticleDTOs = commentArticleService
					.findByArticleId(articleDTO.getId().intValue());

			List<CommentArticleDTO> sortedComments = commentArticleDTOs.stream()
					.sorted(Comparator.comparing(CommentArticleDTO::getNgayDanhGia).reversed()) // mới nhất lên đầu
					.collect(Collectors.toList());

			commentArticleDTO.setListResult(sortedComments);

		}
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user != null) {
			mav.addObject("nameUser", user.getName());
		}

		List<ArticleDTO> ariArticleDTOs=articleDTO.getListResult().stream().filter(article -> article.getStatus() == 1).collect(Collectors.toList());

		articleDTO.setListResult(ariArticleDTOs);
		
		mav.addObject("articles", articleDTO);
		mav.addObject("comments", commentArticleDTO);
		return mav;
	}

}
