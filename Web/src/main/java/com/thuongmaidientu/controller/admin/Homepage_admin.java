package com.thuongmaidientu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.thuongmaidientu.dto.ThongkeDTO;
import com.thuongmaidientu.service.IThongKeService;

@Controller("HomepageAdminOfController")
public class Homepage_admin {

	@Autowired
	private IThongKeService thongKeService;
	
	@RequestMapping(value = "/quan-tri/Homepage_admin", method = RequestMethod.GET)
	public ModelAndView showlist() {

		ModelAndView mav = new ModelAndView("admin/homepage/admin_homepage");

		ThongkeDTO thongkeDTO=new ThongkeDTO();
		thongkeDTO.setQuantityProduct(thongKeService.getCountProduct());
		thongkeDTO.setQuantityPurchase(thongKeService.getCountPurchase());
		thongkeDTO.setQuantityUser(thongKeService.getCountUser());
		thongkeDTO.setQuantityWareHouse(thongKeService.getCountWareHouse());

		mav.addObject("dto",thongkeDTO);
		return mav;
	}

}
