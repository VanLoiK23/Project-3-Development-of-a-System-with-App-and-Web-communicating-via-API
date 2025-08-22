package com.thuongmaidientu.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.DiscountDTO;
import com.thuongmaidientu.service.IDiscountService;

@Controller("discountOfController")
public class DiscountController {

	@Autowired
	private IDiscountService discountService;

	@RequestMapping(value = "/quan-tri/discount/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") DiscountDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/discount/quanli_discount_view");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		model.setListResult(discountService.findAll(pageable));
		model.setTotalItem(discountService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		model.setNum_trash(discountService.getTotalItemTrash());

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/discount/status")
	public String disableKho(@RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam(value = "page", defaultValue = "1") int page, RedirectAttributes redirectAttributes) {
		try {
			if ("disable".equals(type)) {
				discountService.updateStatus(id, 0);
			} else if ("active".equals(type)) {
				discountService.updateStatus(id, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Giữ lại số trang hiện tại khi redirect
		return "redirect:/quan-tri/discount/danh-sach?page=" + page;
	}

	@RequestMapping(value = "/quan-tri/discount/add_Discount", method = RequestMethod.POST)
	public ModelAndView add_Discount(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "discount_code") String discount_code,
			@RequestParam(value = "discount_amount") Integer discount_amount,
			@RequestParam(value = "limit_number") Integer limit_number,
			@RequestParam("expiration_date") 
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date expiration_date,
			@RequestParam(value = "description") String description,
			@RequestParam(value = "payment_limit") Integer payment_limit,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "id", required = false) Integer id, RedirectAttributes redirectAttributes) {

		DiscountDTO dto = new DiscountDTO();
		dto.setCode(discount_code);
		dto.setStatus(status);
		dto.setDiscountAmount(discount_amount);
		dto.setNumberUsed(limit_number);
		dto.setExpirationDate(expiration_date);
		dto.setPaymentLimit(payment_limit);
		dto.setDescription(description);

		if (id != null) {

			dto.setId(Long.valueOf(id));

			discountService.save(dto);
			redirectAttributes.addFlashAttribute("message", "Update Discount successfully!");
		} else {
			discountService.save(dto);

			redirectAttributes.addFlashAttribute("message", "Add Discount successfully!");
		}

		return new ModelAndView("redirect:/quan-tri/discount/danh-sach?page=" + page);
	}

	@GetMapping("/quan-tri/discount/trash")
	public ModelAndView showTrashKho(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/discount/trash_discount");

		DiscountDTO dto = new DiscountDTO();
		dto.setListResult(discountService.findTrash());

		mav.addObject("model", dto);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/discount/reset/{id}")
	public ModelAndView reset(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		discountService.updateTrash(id, "active");
		

		redirectAttributes.addFlashAttribute("message", "Reset discount successfully!");

		return new ModelAndView("redirect:/quan-tri/discount/trash");
	}
	
}
