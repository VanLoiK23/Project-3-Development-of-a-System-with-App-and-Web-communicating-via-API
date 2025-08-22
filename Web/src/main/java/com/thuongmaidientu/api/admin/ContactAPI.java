package com.thuongmaidientu.api.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.ContactDTO;
import com.thuongmaidientu.service.IContactService;

@RestController("contactAPI")
@RequestMapping("/quan-tri/lien-he")
public class ContactAPI {
	@Autowired
	private IContactService contactService;
	
	@GetMapping
	public ResponseEntity<?> showContactContent(@RequestParam("id") int id) {
		try {

			ContactDTO contactDTO = contactService.findById(id);

			return ResponseEntity.ok(Map.of("content", contactDTO.getContent()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

}
