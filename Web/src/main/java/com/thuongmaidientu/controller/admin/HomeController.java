package com.thuongmaidientu.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "HomeControllerOfAdmin")
public class HomeController {
	
	   @RequestMapping(value = "/quan-tri/home", method = RequestMethod.GET)
	   public ModelAndView homePage() {
	      ModelAndView mav = new ModelAndView("admin/home");
	      return mav;
	   }
	  
	   @RequestMapping(value = "/springmvc", method = RequestMethod.GET)
	   public ModelAndView springMVCPage() {
	      ModelAndView mav = new ModelAndView("springmvc");
	      return mav;
	   }
}
