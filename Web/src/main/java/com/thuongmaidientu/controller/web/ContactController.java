package com.thuongmaidientu.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.ContactDTO;
import com.thuongmaidientu.service.IContactService;

@Controller("contactOfWebController")
public class ContactController {

	@Autowired
	private IContactService contactService;

	@GetMapping("/contact")
	public ModelAndView show(@ModelAttribute("message") String message) {
		return new ModelAndView("web/contact/lienhe");
	}

	@PostMapping("/contact")
	public ModelAndView sendContact(RedirectAttributes redirectAttributes,
			@RequestParam("con_name") String name,@RequestParam("con_email") String email,@RequestParam("con_content") String content,@RequestParam("con_message") String message) {
		
		if(name.isEmpty()||email.isEmpty()||content.isEmpty()||message.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Vui lòng điền đủ thông tin!");
			
			return new ModelAndView("redirect:/contact");
		}else {
			
			
			ContactDTO contactDTO=new ContactDTO();
			contactDTO.setTenKhachHang(name);
			contactDTO.setEmail(email);
			contactDTO.setTitle(content);
			contactDTO.setContent(message);
			contactDTO.setStatus(0);
			
			contactService.save(contactDTO);
			
			redirectAttributes.addFlashAttribute("message", "Gửi thông tin liên hệ thành công!");

			return new ModelAndView("redirect:/contact");

		}

	}

}
