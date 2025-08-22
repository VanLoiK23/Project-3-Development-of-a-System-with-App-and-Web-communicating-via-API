package com.thuongmaidientu.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.ContactDTO;
import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.dto.RomDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.service.IContactService;
import com.thuongmaidientu.service.IKhuVucKhoService;
import com.thuongmaidientu.service.INhacungcapService;

@Controller("contactOfController")
@RequestMapping("/quan-tri/contact")
public class ContactController {

	@Autowired
	private IContactService contactService;

	@RequestMapping(value = "/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") ContactDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/contact/lienhe");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		model.setListResult(contactService.findAll(pageable));
		model.setTotalItem(contactService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@PostMapping("/sendEmail")
	public ModelAndView sendEmail(@RequestParam("id") Integer contactId, @RequestParam("name") String nameSender,
			@RequestParam("email") String emailClient, @RequestParam("ct") String contentSend,
			@RequestParam("khachhang") String nameClient, RedirectAttributes redirectAttributes) {

		contactService.updateStatus(contactId, 1, emailClient, contentSend, nameClient, nameSender);

		redirectAttributes.addFlashAttribute("message", "Phản hồi thành công!");

		return new ModelAndView("redirect:/quan-tri/contact/danh-sach");
	}

	@DeleteMapping
	@ResponseBody
	public ResponseEntity<?> deleteContact(@RequestParam("id") Integer contactId) {
		try {
			contactService.delete(contactId);

			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body(false);
		}
	}

}
