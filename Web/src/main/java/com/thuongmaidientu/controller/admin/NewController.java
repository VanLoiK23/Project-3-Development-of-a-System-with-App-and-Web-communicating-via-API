//package com.thuongmaidientu.controller.admin;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.thuongmaidientu.dto.NewDTO;
//import com.thuongmaidientu.dto.ProductDTO;
//import com.thuongmaidientu.model.NewModel;
//import com.thuongmaidientu.service.IProductService;
//
//@Controller(value = "NewControllerOfAdmin")
//public class NewController {
//
//	@Autowired
//	private IProductService productService;
//
//	@RequestMapping(value = "/quan-tri/bai-viet/danh-sach", method = RequestMethod.GET)
//	public ModelAndView showlist(
//	        @ModelAttribute("model") ProductDTO model,
//	        @RequestParam(value = "page", defaultValue = "1") int page,
//	        @RequestParam(value = "limit", defaultValue = "5") int limit) { // Giá trị mặc định là 1 và 5
//
//	    ModelAndView mav = new ModelAndView("admin/new/list");
//
//	    model.setPage(page);
//	    model.setLimit(limit);
//
//	    Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));
//
//	    model.setListResult(productService.findAll(pageable));
//	    model.setTotalItem(productService.getTotalItem());
//	    model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));
//
//	    mav.addObject("model", model);
//	    return mav;
//	}
//
//
//	@RequestMapping(value = "/quan-tri/bai-viet/chinh-sua", method = RequestMethod.GET)
//	public ModelAndView edit() {
//		ModelAndView mav = new ModelAndView("admin/new/edit");
//		return mav;
//	}
//
//	@RequestMapping(value = "/quan-tri/bai-viet/them", method = RequestMethod.GET)
//	public ModelAndView add() {
//		ModelAndView mav = new ModelAndView("admin/home");
//
//		return mav;
//	}
//
////	@RequestMapping(value = "/quan-tri/bai-viet/them", method = RequestMethod.POST)
////	public ModelAndView add(@ModelAttribute("model") NewDTO newDTO) {
////		productService.save(newDTO); // Lưu bài viết vào DB
////		return new ModelAndView("redirect:/quan-tri/bai-viet/danh-sach"); // Chuyển về danh sách
////	}
//
//}
