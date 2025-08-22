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
import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.dto.RomDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.service.IKhuVucKhoService;

@Controller("khoOfController")
public class KhuVucKhoController {

	@Autowired
	private IKhuVucKhoService khuVucKhoService;

	@RequestMapping(value = "/quan-tri/khuvuckho/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") KhuVucKhoDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/warehouse/quanli_kho_view");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("maKhuVuc")));

		model.setListResult(khuVucKhoService.findAll(pageable));
		model.setTotalItem(khuVucKhoService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		model.setNum_trash(khuVucKhoService.getTotalItemTrash());

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/khuvuckho/status")
	public String disableKho(@RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam(value = "page", defaultValue = "1") int page, RedirectAttributes redirectAttributes) {
		try {
			if ("disable".equals(type)) {
				khuVucKhoService.updateStatus(id, 0);
			} else if ("active".equals(type)) {
				khuVucKhoService.updateStatus(id, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Giữ lại số trang hiện tại khi redirect
		return "redirect:/quan-tri/khuvuckho/danh-sach?page=" + page;
	}

	@RequestMapping(value = "/quan-tri/khuvuckho/add_KVKho", method = RequestMethod.POST)
	public ModelAndView add_Kho(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "name") String name, @RequestParam(value = "ghichu", required = false) String ghichu,
			@RequestParam(value = "id", required = false) Integer id, RedirectAttributes redirectAttributes) {

		KhuVucKhoDTO dto = new KhuVucKhoDTO();
		dto.setTenKhuVuc(name);
		if (ghichu != null) {
			dto.setGhiChu(ghichu);
		}

		if (id != null) {

			dto.setId(Long.valueOf(id));

			khuVucKhoService.save(dto);
			redirectAttributes.addFlashAttribute("message", "Update WareHouse successfully!");
		} else {
			khuVucKhoService.save(dto);

			redirectAttributes.addFlashAttribute("message", "Add WareHouse successfully!");
		}

		return new ModelAndView("redirect:/quan-tri/khuvuckho/danh-sach?page=" + page);
	}

	@GetMapping("/quan-tri/khuvuckho/trash")
	public ModelAndView showTrashKho(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/warehouse/trash_KVKho");

		KhuVucKhoDTO dto = new KhuVucKhoDTO();
		dto.setListResult(khuVucKhoService.findTrash());

		mav.addObject("model", dto);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/khuvuckho/reset/{id}")
	public ModelAndView reset(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		khuVucKhoService.updateTrash(id, "active");

		redirectAttributes.addFlashAttribute("message", "Reset WareHouse successfully!");

		return new ModelAndView("redirect:/quan-tri/khuvuckho/trash");
	}
}
