package com.thuongmaidientu.controller.admin;

import java.util.ArrayList;
import java.util.List;

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
import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.dto.RomDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.service.CloudinaryService;
import com.thuongmaidientu.service.ICategotyService;
import com.thuongmaidientu.service.IColorService;
import com.thuongmaidientu.service.IHedieuhanhService;
import com.thuongmaidientu.service.IRamService;
import com.thuongmaidientu.service.IRomService;
import com.thuongmaidientu.service.IXuatxuService;

@Controller(value = "Attribute_sp_controller")
public class Attribute_sp_controller {
	@Autowired
	private ICategotyService categoryService;

	@Autowired
	private IHedieuhanhService hedieuhanhService;

	@Autowired
	private IColorService colorService;

	@Autowired
	private IRamService ramService;

	@Autowired
	private IRomService romService;

	@Autowired
	private IXuatxuService xuatxuService;

	@Autowired
	private CloudinaryService cloudinaryService;

	@RequestMapping(value = "/quan-tri/attribute-sp/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@RequestParam(value = "type", defaultValue = "xuatxu") String type,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/attribute/quanli_attribute_view");

		if ("xuatxu".equals(type)) {
			XuatXuDTO model = new XuatXuDTO();

			model.setPage(page);
			model.setLimit(limit);

			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("maXuatXu")));

			model.setListResult(xuatxuService.findAll(pageable));
			model.setTotalItem(xuatxuService.getTotalItem());
			model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

			model.setNum_trash(xuatxuService.getTotalItemTrash());

			mav.addObject("model", model);
			mav.addObject("page", page);
			mav.addObject("tenAttri", "Xuất xứ");

		} else if ("hedieuhanh".equals(type)) {
			HedieuhanhDTO model = new HedieuhanhDTO();

			model.setPage(page);
			model.setLimit(limit);

			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("maHeDieuHanh")));

			model.setListResult(hedieuhanhService.findAll(pageable));
			model.setTotalItem(hedieuhanhService.getTotalItem());
			model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

			model.setNum_trash(hedieuhanhService.getTotalItemTrash());

			mav.addObject("model", model);
			mav.addObject("page", page);
			mav.addObject("tenAttri", "Hệ điều hành");

		} else if ("thuonghieu".equals(type)) {
			CategoryDTO model = new CategoryDTO();

			model.setPage(page);
			model.setLimit(limit);

			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("maThuongHieu")));

			model.setListResult(categoryService.findAll(pageable));
			model.setTotalItem(categoryService.getTotalItem());
			model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

			model.setNum_trash(categoryService.getTotalItemTrash());

			mav.addObject("model", model);
			mav.addObject("page", page);
			mav.addObject("tenAttri", "Thương hiệu");

		} else if ("dungluongram".equals(type)) {
			RamDTO model = new RamDTO();

			model.setPage(page);
			model.setLimit(limit);

			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("maDungLuongRam")));

			model.setListResult(ramService.findAll(pageable));
			model.setTotalItem(ramService.getTotalItem());
			model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

			model.setNum_trash(ramService.getTotalItemTrash());

			mav.addObject("model", model);
			mav.addObject("page", page);
			mav.addObject("tenAttri", "Kích thước Ram");

		} else if ("dungluongrom".equals(type)) {
			RomDTO model = new RomDTO();

			model.setPage(page);
			model.setLimit(limit);

			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("maDungLuongRom")));

			model.setListResult(romService.findAll(pageable));
			model.setTotalItem(romService.getTotalItem());
			model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

			model.setNum_trash(romService.getTotalItemTrash());

			mav.addObject("model", model);
			mav.addObject("page", page);
			mav.addObject("tenAttri", "Kích thước Rom");

		} else {
			MauSacDTO model = new MauSacDTO();

			model.setPage(page);
			model.setLimit(limit);

			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("maMauSac")));

			model.setListResult(colorService.findAll(pageable));
			model.setTotalItem(colorService.getTotalItem());
			model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

			model.setNum_trash(colorService.getTotalItemTrash());

			mav.addObject("model", model);
			mav.addObject("page", page);
			mav.addObject("tenAttri", "Màu sắc");

		}

		mav.addObject("type", type);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/attribute-sp/status")
	public String disableSanPham(@RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam("action") String action, @RequestParam(value = "page", defaultValue = "1") int page,
			RedirectAttributes redirectAttributes) {
		try {
			if ("disable".equals(action)) {
//				productService.updateStatus(id, 0);

				if ("xuatxu".equals(type)) {
					xuatxuService.updateStatus(id, 0);
				} else if ("hedieuhanh".equals(type)) {
					hedieuhanhService.updateStatus(id, 0);
				} else if ("thuonghieu".equals(type)) {
					categoryService.updateStatus(id, 0);
				} else if ("dungluongram".equals(type)) {
					ramService.updateStatus(id, 0);
				} else if ("dungluongrom".equals(type)) {
					romService.updateStatus(id, 0);
				} else {
					colorService.updateStatus(id, 0);
				}
			} else if ("active".equals(action)) {
//				productService.updateStatus(id, 1);
				if ("xuatxu".equals(type)) {
					xuatxuService.updateStatus(id, 1);
				} else if ("hedieuhanh".equals(type)) {
					hedieuhanhService.updateStatus(id, 1);
				} else if ("thuonghieu".equals(type)) {
					categoryService.updateStatus(id, 1);
				} else if ("dungluongram".equals(type)) {
					ramService.updateStatus(id, 1);
				} else if ("dungluongrom".equals(type)) {
					romService.updateStatus(id, 1);
				} else {
					colorService.updateStatus(id, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Giữ lại số trang hiện tại khi redirect
		return "redirect:/quan-tri/attribute-sp/danh-sach?page=" + page + "&type=" + type;
	}

	@RequestMapping(value = "/quan-tri/attribute-sp/add_Attribute", method = RequestMethod.POST)
	public ModelAndView add_Attribute(@RequestParam(value = "type", defaultValue = "xuatxu") String type,
			@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "value") String value,
			@RequestParam(value = "img", required = false) MultipartFile img,
			@RequestParam(value = "id", required = false) Integer id, RedirectAttributes redirectAttributes) {

		if ("xuatxu".equals(type)) {
			XuatXuDTO dto = new XuatXuDTO();
			dto.setTenXuatXu(value);
			if (id != null) {
				dto.setId(Long.valueOf(id));
			}

			xuatxuService.save(dto);
		} else if ("hedieuhanh".equals(type)) {
			HedieuhanhDTO dto = new HedieuhanhDTO();
			dto.setTenHeDieuHanh(value);
			if (id != null) {
				dto.setId(Long.valueOf(id));
			}

			hedieuhanhService.save(dto);
		} else if ("thuonghieu".equals(type)) {
			CategoryDTO dto = new CategoryDTO();
			if (img != null && !img.isEmpty()) {
				String newImageUrl = cloudinaryService.uploadFile(img, value); // Upload ảnh mới
				dto.setImage(newImageUrl);

			}

			dto.setTenThuongHieu(value);
			if (id != null) {
				dto.setId(Long.valueOf(id));

				if (dto.getImage() == null) {
					dto.setImage(categoryService.getImageOld(id));
				}
			}

			categoryService.save(dto);

		} else if ("dungluongram".equals(type)) {
			RamDTO dto = new RamDTO();
			dto.setKichThuocRam(Integer.parseInt(value));
			if (id != null) {
				dto.setId(Long.valueOf(id));
			}

			ramService.save(dto);
		} else if ("dungluongrom".equals(type)) {
			RomDTO dto = new RomDTO();
			dto.setKichThuocRom(Integer.parseInt(value));
			if (id != null) {
				dto.setId(Long.valueOf(id));
			}

			romService.save(dto);
		} else {
			MauSacDTO dto = new MauSacDTO();
			dto.setTenMauSac(value);
			if (id != null) {
				dto.setId(Long.valueOf(id));
			}

			colorService.save(dto);
		}
		if (id != null) {
			redirectAttributes.addFlashAttribute("message", "Update attribute successfully!");
		} else {
			redirectAttributes.addFlashAttribute("message", "Add attribute successfully!");
		}

		return new ModelAndView("redirect:/quan-tri/attribute-sp/danh-sach?type=" + type + "&page=" + page);
	}

	@GetMapping("/quan-tri/attribute-sp/trash")
	public ModelAndView showTrashAttribute(@RequestParam("type") String type,
			@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/attribute/trash_attribute");

		if ("xuatxu".equals(type)) {
			XuatXuDTO dto = new XuatXuDTO();
			dto.setListResult( xuatxuService.findTrash());

			mav.addObject("model", dto);
		} else if ("hedieuhanh".equals(type)) {
			HedieuhanhDTO dto = new HedieuhanhDTO();
			dto.setListResult( hedieuhanhService.findTrash());

			mav.addObject("model", dto);
		} else if ("thuonghieu".equals(type)) {
			CategoryDTO dto = new CategoryDTO();
			dto.setListResult( categoryService.findTrash());

			mav.addObject("model", dto);
		} else if ("dungluongram".equals(type)) {
			RamDTO dto = new RamDTO();
			dto.setListResult( ramService.findTrash());
			
			mav.addObject("model", dto);
		} else if ("dungluongrom".equals(type)) {
			RomDTO dto = new RomDTO();
			dto.setListResult( romService.findTrash());

			mav.addObject("model", dto);
		} else {
			MauSacDTO dto = new MauSacDTO();
			dto.setListResult( colorService.findTrash());

			mav.addObject("model", dto);
		}
		
		mav.addObject("type", type);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/attribute-sp/reset")
	public ModelAndView reset(@RequestParam("id") Integer id, @RequestParam("type") String type,
			RedirectAttributes redirectAttributes) {

		if ("xuatxu".equals(type)) {
			xuatxuService.updateTrash(id, "active");
		} else if ("hedieuhanh".equals(type)) {
			hedieuhanhService.updateTrash(id, "active");
		} else if ("thuonghieu".equals(type)) {
			categoryService.updateTrash(id, "active");
		} else if ("dungluongram".equals(type)) {
			ramService.updateTrash(id, "active");
		} else if ("dungluongrom".equals(type)) {
			romService.updateTrash(id, "active");
		} else {
			colorService.updateTrash(id, "active");
		}

		redirectAttributes.addFlashAttribute("message", "Reset Attribute successfully!");

		return new ModelAndView("redirect:/quan-tri/attribute-sp/trash?type=" + type);
	}
}
