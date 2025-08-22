package com.thuongmaidientu.controller.admin;

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

import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.dto.RomDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.service.IKhuVucKhoService;
import com.thuongmaidientu.service.INhacungcapService;

@Controller("supplierOfController")
public class NhaCungCapController {

	@Autowired
	private INhacungcapService nhacungcapService;

	@RequestMapping(value = "/quan-tri/supplier/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") NhaCungCapDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/supplier/quanli_NCC_view");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		model.setListResult(nhacungcapService.findAll(pageable));
		model.setTotalItem(nhacungcapService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		model.setNum_trash(nhacungcapService.getTotalItemTrash());

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/supplier/status")
	public String disableKho(@RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam(value = "page", defaultValue = "1") int page, RedirectAttributes redirectAttributes) {
		try {
			if ("disable".equals(type)) {
				nhacungcapService.updateStatus(id, 0);
			} else if ("active".equals(type)) {
				nhacungcapService.updateStatus(id, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Giữ lại số trang hiện tại khi redirect
		return "redirect:/quan-tri/supplier/danh-sach?page=" + page;
	}

	@RequestMapping(value = "/quan-tri/supplier/add_NCC", method = RequestMethod.POST)
	public ModelAndView add_NCC(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "name") String name, @RequestParam(value = "address") String address,
			@RequestParam(value = "email") String email, @RequestParam(value = "sdt") String sdt,
			@RequestParam(value = "id", required = false) Integer id, RedirectAttributes redirectAttributes) {

		NhaCungCapDTO dto = new NhaCungCapDTO();
		dto.setTenNhaCungCap(name);
		dto.setDiaChi(address);
		dto.setEmail(email);
		dto.setSoDienThoai(sdt);

		if (id != null) {

			dto.setId(Long.valueOf(id));

			nhacungcapService.save(dto);
			redirectAttributes.addFlashAttribute("message", "Update Supplier successfully!");
		} else {
			nhacungcapService.save(dto);

			redirectAttributes.addFlashAttribute("message", "Add Supplier successfully!");
		}

		return new ModelAndView("redirect:/quan-tri/supplier/danh-sach?page=" + page);
	}

	@GetMapping("/quan-tri/supplier/trash")
	public ModelAndView showTrashKho(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/supplier/trash_NCC");

		NhaCungCapDTO dto = new NhaCungCapDTO();
		dto.setListResult(nhacungcapService.findTrash());

		mav.addObject("model", dto);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/supplier/reset/{id}")
	public ModelAndView reset(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		nhacungcapService.updateTrash(id, "active");

		redirectAttributes.addFlashAttribute("message", "Reset supplier successfully!");

		return new ModelAndView("redirect:/quan-tri/supplier/trash");
	}
}
