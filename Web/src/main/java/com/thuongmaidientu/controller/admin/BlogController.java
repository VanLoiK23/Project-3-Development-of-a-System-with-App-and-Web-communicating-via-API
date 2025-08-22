package com.thuongmaidientu.controller.admin;

import java.util.Date;

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

import com.thuongmaidientu.dto.ArticleDTO;
import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.dto.RomDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.entity.ArticleEntity;
import com.thuongmaidientu.service.CloudinaryService;
import com.thuongmaidientu.service.IArticleService;
import com.thuongmaidientu.service.IKhuVucKhoService;
import com.thuongmaidientu.service.INhacungcapService;
import com.thuongmaidientu.service.IProductService;

@Controller("blogOfController")
public class BlogController {

	@Autowired
	private IArticleService articleService;

	@Autowired
	private CloudinaryService cloudinaryService;

	@RequestMapping(value = "/quan-tri/Articles/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") ArticleDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/blog/quanli_Articles_view");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		model.setListResult(articleService.findAll(pageable));
		model.setTotalItem(articleService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		model.setNum_trash(articleService.getTotalItemTrash());

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/Articles/status")
	public String updateStatusArticle(@RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam(value = "page", defaultValue = "1") int page, RedirectAttributes redirectAttributes) {
		try {
			if ("disable".equals(type)) {
				articleService.updateStatus(id, 0);
			} else if ("active".equals(type)) {
				articleService.updateStatus(id, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Giữ lại số trang hiện tại khi redirect
		return "redirect:/quan-tri/Articles/danh-sach?page=" + page;
	}

	@GetMapping("/quan-tri/Articles/add_Articles")
	public ModelAndView showFormAddArticle() {
		return new ModelAndView("admin/blog/add_Articles");
	}

	@GetMapping("/quan-tri/Articles/edit_Articles")
	public ModelAndView showFormUpdateArticle(@RequestParam("id") Integer id) {
		ModelAndView mav = new ModelAndView("admin/blog/edit_Articles");

		ArticleDTO articleDTO = articleService.findById(id);
		mav.addObject("model", articleDTO);
		return mav;
	}

	@RequestMapping(value = "/quan-tri/Articles/add_Articles", method = RequestMethod.POST)
	public ModelAndView addBlog(@ModelAttribute("model") ArticleDTO dto,
			@RequestParam(value = "details", required = false) String details,
			@RequestParam(value = "sortDesc") String sortDesc, @RequestParam(value = "title") String title,
			@RequestParam(value = "img", required = false) MultipartFile img,
			@RequestParam(value = "id", required = false) Integer id, RedirectAttributes redirectAttributes) {
		
		// Update
		if (id != null) {
			dto = articleService.findById(id);
		}
		
		if (img != null && !img.isEmpty()) {
			String randomName = "img_" + System.currentTimeMillis();
			dto.setImage(cloudinaryService.uploadFile(img, randomName));
		} else {
			// Forgotten select img
			if (id == null) {
				redirectAttributes.addFlashAttribute("message", "Vui lòng chọn thumbnail cho Article!");

				return new ModelAndView("redirect:/quan-tri/Articles/add_Articles");
			}
		}
		dto.setSortDesc(sortDesc);
		dto.setTitle(title);
		dto.setDetails(details);

		if (id != null) {
//			dto.setId(Long.valueOf(id));

			articleService.save(dto);
			redirectAttributes.addFlashAttribute("message", "Update Blog successfully!");
		} else {
			dto.setStatus(1);
			dto.setTrash("active");
			dto.setCreatedDate(new Date());

			articleService.save(dto);

			redirectAttributes.addFlashAttribute("message", "Add Blog successfully!");
		}

		return new ModelAndView("redirect:/quan-tri/Articles/danh-sach");
	}

	@GetMapping("/quan-tri/Articles/trash")
	public ModelAndView showTrashKho(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/blog/trash_Articles");

		ArticleDTO dto = new ArticleDTO();
		dto.setListResult(articleService.findTrash());

		mav.addObject("model", dto);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/Articles/reset/{id}")
	public ModelAndView reset(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		articleService.updateTrash(id, "active");

		redirectAttributes.addFlashAttribute("message", "Reset blog successfully!");

		return new ModelAndView("redirect:/quan-tri/Articles/trash");
	}

	@GetMapping("/quan-tri/Articles/Delete_permanent/{id}")
	public ModelAndView deletePermanent(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

		articleService.delete(id);

		redirectAttributes.addFlashAttribute("message", "Delete blog successfully!");

		return new ModelAndView("redirect:/quan-tri/Articles/trash");
	}
}
