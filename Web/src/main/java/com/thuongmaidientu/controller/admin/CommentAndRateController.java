package com.thuongmaidientu.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.ICommentAndRateService;

import jakarta.servlet.http.HttpSession;

@Controller("commentOfController")
public class CommentAndRateController {

	@Autowired
	private ICommentAndRateService commentAndRateService;

	@RequestMapping(value = "/quan-tri/Quanlicomment_controller/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") CommentAndRateDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit,
			@RequestParam(value = "rateFilter", required = false) Integer rateFilter,
			@RequestParam(value = "statusFilter", required = false) Integer statusFilter,
			@ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/comment/comment");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		List<CommentAndRateDTO> list;
		int totalItem;

		if (rateFilter != null || statusFilter != null) {
			list = commentAndRateService.findAllByRateAndStatus(rateFilter, statusFilter, pageable);
			totalItem = commentAndRateService.getTotalItemByRateAndStatus(rateFilter, statusFilter);

		} else {
			list = commentAndRateService.findAll(pageable);
			totalItem = commentAndRateService.getTotalItem();
		}

		if (rateFilter == null && statusFilter == null) {
			list = commentAndRateService.findAll(pageable);
			totalItem = commentAndRateService.getTotalItem();
		}

		model.setListResult(list);
		model.setTotalItem(totalItem);
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		mav.addObject("model", model);
		mav.addObject("page", page);
		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message);
		}
		mav.addObject("rateFilter", rateFilter);
		mav.addObject("statusFilter", statusFilter);
		if (statusFilter == null) {
			mav.addObject("statusFilter", null);
		}
		if (rateFilter == null) {
			mav.addObject("rateFilter", null);
		}

		return mav;
	}

	@RequestMapping(value = "/quan-tri/Quanlicomment_controller/show_content/{id}", method = RequestMethod.GET)
	public ModelAndView showlist(@PathVariable("id") Integer id) {

		ModelAndView mav = new ModelAndView("admin/comment/comment_show");

		CommentAndRateDTO dto = new CommentAndRateDTO();
		dto = commentAndRateService.findById(id);

		mav.addObject("dto", dto);
		System.out.println("Debug " + dto);

		return mav;
	}

	@RequestMapping(value = "/quan-tri/Quanlicomment_controller/show_content/{id}", method = RequestMethod.POST)
	public ModelAndView reviewHandle(@PathVariable("id") Integer id,
			@RequestParam(value = "feeback_content", defaultValue = "", required = true) String feeback_content,
			RedirectAttributes redirectAttributes, HttpSession session) {

		CommentAndRateDTO dto = new CommentAndRateDTO();
		dto.setId((long) id);
		dto.setFeeback(1);
		dto.setFeedbackContent(feeback_content);
		UserDTO user = (UserDTO) session.getAttribute("user");
		if (user == null) {
			return new ModelAndView("redirect:/dang-nhap"); // chưa đăng nhập
		}else {
			dto.setNhanvien(user.getName());
		}

		Date today = new Date();

		dto.setNgayPH(today);

		commentAndRateService.updateFeeback(dto);

		redirectAttributes.addFlashAttribute("message", "Phản hồi bình luận thành công!");

		return new ModelAndView("redirect:/quan-tri/Quanlicomment_controller/danh-sach");

	}
}
