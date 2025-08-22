package com.thuongmaidientu.controller.admin;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.SlideDTO;
import com.thuongmaidientu.service.CloudinaryService;
import com.thuongmaidientu.service.ISlideService;

@Controller("slideOfController")
public class SlideController {

	@Autowired
	private ISlideService slideService;

	@Autowired
	private CloudinaryService cloudinaryService;

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int LENGTH = 10;

	@RequestMapping(value = "/quan-tri/slide/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") SlideDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/slide/quanli_slide_view");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		model.setListResult(slideService.findAll(pageable));
		model.setTotalItem(slideService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		model.setNum_trash(slideService.getTotalItemTrash());

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/slide/status")
	public String disableKho(@RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam(value = "page", defaultValue = "1") int page, RedirectAttributes redirectAttributes) {
		try {
			if ("disable".equals(type)) {
				slideService.updateStatus(id, 0);
			} else if ("active".equals(type)) {
				slideService.updateStatus(id, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Giữ lại số trang hiện tại khi redirect
		return "redirect:/quan-tri/slide/danh-sach?page=" + page;
	}

	@RequestMapping(value = "/quan-tri/slide/add_Slide", method = RequestMethod.POST)
	public ModelAndView add_NCC(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "img") MultipartFile img, @RequestParam(value = "id", required = false) Integer id,
			RedirectAttributes redirectAttributes) {

		SlideDTO dto = new SlideDTO();

		if (img != null && !img.isEmpty()) {
			String newImageUrl = cloudinaryService.uploadFile(img, generateRandomString()); // Upload ảnh mới
			dto.setImage(newImageUrl);
		}
		
		if (id != null) {
			dto.setId(Long.valueOf(id));

			slideService.save(dto);
			redirectAttributes.addFlashAttribute("message", "Update Slide successfully!");
		} else {
			slideService.save(dto);

			redirectAttributes.addFlashAttribute("message", "Add Slide successfully!");
		}

		return new ModelAndView("redirect:/quan-tri/slide/danh-sach?page=" + page);
	}

	@GetMapping("/quan-tri/slide/trash")
	public ModelAndView showTrashKho(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/slide/trash_slide");

		SlideDTO dto = new SlideDTO();
		dto.setListResult(slideService.findTrash());

		mav.addObject("model", dto);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/slide/reset/{id}")
	public ModelAndView reset(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		slideService.updateTrash(id, "active");

		redirectAttributes.addFlashAttribute("message", "Reset slide successfully!");

		return new ModelAndView("redirect:/quan-tri/slide/trash");
	}

	public static String generateRandomString() {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(LENGTH);
		for (int i = 0; i < LENGTH; i++) {
			int index = random.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}
		return sb.toString();
	}
}
