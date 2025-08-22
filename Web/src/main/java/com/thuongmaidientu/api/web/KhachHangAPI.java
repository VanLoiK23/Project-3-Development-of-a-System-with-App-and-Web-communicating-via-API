package com.thuongmaidientu.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.service.IKhachHangService;

@RestController(value = "clientApiOfWeb")
@RequestMapping("/khach-hang")
public class KhachHangAPI {

	@Autowired
	IKhachHangService khachHangService;

	@PostMapping("/checkUser_name")
	public ResponseEntity<?> checkIsExistUserName(@RequestParam("name") String name) {
		try {
			return ResponseEntity.ok(khachHangService.isExistUserName(name));
		} catch (Exception e) {
			e.printStackTrace(); // ✅ In ra lỗi đầy đủ
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}
	
	@PostMapping("/checkEmail")
	public ResponseEntity<?> checkIsExistEmail(@RequestParam("email") String email) {
		try {
			return ResponseEntity.ok(khachHangService.isExistEmail(email));
		} catch (Exception e) {
			e.printStackTrace(); // ✅ In ra lỗi đầy đủ
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

	@PostMapping("/checkAccountIsLock")
	public ResponseEntity<?> checkAccountIsLock(
			@RequestParam(value = "email", required = false, defaultValue = "") String email,
			@RequestParam(value = "phone", required = false, defaultValue = "") String phone) {
		try {

			email = (email == null || email.isEmpty()) ? null : email;
			phone = (phone == null || phone.isEmpty()) ? null : phone;

			return ResponseEntity.ok(khachHangService.checkIsLockByPhoneOrEmail(email, phone));
		} catch (Exception e) {
			e.printStackTrace(); // ✅ In ra lỗi đầy đủ
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

}
