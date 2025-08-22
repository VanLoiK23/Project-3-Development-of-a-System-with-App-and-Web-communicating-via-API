package com.thuongmaidientu.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("introduceOfWebController")
public class IntroduceController {

	@GetMapping("/introduce")
	public ModelAndView show() {
		return new ModelAndView("web/introduce/gioithieu_view");
	}
}
