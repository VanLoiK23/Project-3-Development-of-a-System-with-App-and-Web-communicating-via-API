package com.thuongmaidientu.controller.admin;

import java.text.Normalizer;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.IKhachHangService;

@Controller("clientOfController")
public class Quanlikhachhang_controller {

	@Autowired
	private IKhachHangService khachHangService;

	@RequestMapping(value = "/quan-tri/Quanlikhachhang_controller/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") UserDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/khachhang/quanli_KH_view");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		model.setListResult(khachHangService.findAll(pageable));
		model.setTotalItem(khachHangService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		model.setNum_trash(khachHangService.getTotalItemTrash());

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@RequestMapping(value = "/quan-tri/Quanlikhachhang_controller/edit_KH", method = RequestMethod.POST)
	public ModelAndView edit_KH(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "name") String name, @RequestParam(value = "soDienThoai") String soDienThoai,
			@RequestParam(value = "email") String email, @RequestParam(value = "ngaysinh") java.sql.Date ngaysinh,
			@RequestParam(value = "gender") String gender, @RequestParam(value = "id", required = false) Integer id,
			RedirectAttributes redirectAttributes) {

		UserDTO dto = new UserDTO();
		dto.setName(name);
		dto.setEmail(email);
		dto.setPhone(soDienThoai);
		dto.setGender(gender);
		dto.setNgaySinh(ngaysinh);
		dto.setId(id.longValue());
		dto.setUsername(formatName(name));
//
//		KhachHangDTO dtoKH = new KhachHangDTO();
//		dtoKH.setHoTen(name);
//		dtoKH.setEmail(email);
//		dtoKH.setSoDienThoai(soDienThoai);
//		dtoKH.setGioiTinh(gender);
//		dtoKH.setNgaySinh(ngaysinh);
//		dtoKH.setId(id.longValue());
//
//		khachHangService.save(dto);
//		khachHangService.save(dtoKH);
		
		khachHangService.update(dto);

		redirectAttributes.addFlashAttribute("message", "Update Client successfully!");

		return new ModelAndView("redirect:/quan-tri/Quanlikhachhang_controller/danh-sach?page=" + page);
	}

	@GetMapping("/quan-tri/Quanlikhachhang_controller/trash")
	public ModelAndView showTrashKho(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/khachhang/trash_KH");

		UserDTO dto = new UserDTO();
		dto.setListResult(khachHangService.findTrash());

		mav.addObject("model", dto);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/Quanlikhachhang_controller/reset/{id}")
	public ModelAndView reset(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		khachHangService.updateStatus(id, "active");

		redirectAttributes.addFlashAttribute("message", "Reset client successfully!");

		return new ModelAndView("redirect:/quan-tri/Quanlikhachhang_controller/trash");
	}
	
	public static String formatName(String input) {
        // 1. Chuẩn hóa và loại bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noDiacritics = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // 2. Tách các từ, viết hoa và nối lại
        String[] parts = noDiacritics.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                // Viết hoa chữ cái đầu mỗi từ, các ký tự còn lại viết thường
                result.append(Character.toUpperCase(part.charAt(0)))
                      .append(part.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }
}
