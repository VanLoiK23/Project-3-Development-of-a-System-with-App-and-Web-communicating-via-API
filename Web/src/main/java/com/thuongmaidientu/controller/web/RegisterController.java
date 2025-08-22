package com.thuongmaidientu.controller.web;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.firebase.auth.FirebaseAuthException;
import com.thuongmaidientu.dto.KhachHangDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.FirebaseService;
import com.thuongmaidientu.service.IKhachHangService;

@Controller("registerController")
public class RegisterController {
	@Autowired
	private IKhachHangService khachHangService;

	@Autowired
	private FirebaseService firebaseService;

	@RequestMapping(value = "/dang-ki", method = RequestMethod.GET)
	public ModelAndView showFormRegister(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("register");

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message);
		}
		return mav;
	}

	@RequestMapping(value = "/dang-ki", method = RequestMethod.POST)
	public String register(@RequestParam("first_name") String first_name, @RequestParam("last_name") String last_name,
			@RequestParam("phone") String phone, @RequestParam("password") String password,
			@RequestParam("email") String email,
			@RequestParam("ngaysinh") @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngaysinh,
			@RequestParam("gender") String gender, RedirectAttributes redirectAttributes)
			throws FirebaseAuthException, ExecutionException, InterruptedException {

		// Kiểm tra trống
		if (first_name.isEmpty() || last_name.isEmpty() || phone.isEmpty() || password.isEmpty() || email.isEmpty()
				|| ngaysinh == null || gender.isEmpty()) {

			redirectAttributes.addFlashAttribute("message", "Vui lòng điền đủ các trường!");
			return "redirect:/dang-ki";
		}

		// Kiểm tra số điện thoại phải có đúng 10 chữ số
		if (!phone.matches("\\d{10}")) {
			redirectAttributes.addFlashAttribute("message", "Số điện thoại phải gồm đúng 10 chữ số!");
			return "redirect:/dang-ki";
		}

		// Kiểm tra mật khẩu có ít nhất 6 ký tự
		if (password.length() < 6) {
			redirectAttributes.addFlashAttribute("message", "Mật khẩu phải có ít nhất 6 ký tự!");
			return "redirect:/dang-ki";
		}

		// Kiểm tra định dạng email đơn giản
		if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
			redirectAttributes.addFlashAttribute("message", "Email không hợp lệ!");
			return "redirect:/dang-ki";
		}

		// TODO: xử lý lưu user vào DB
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

		UserDTO user = new UserDTO();

		/*
		 * try { Date birthDate = sdf.parse(ngaysinh);
		 * 
		 * user.setNgaySinh(birthDate);
		 * 
		 * } catch (ParseException e) { e.printStackTrace(); }
		 */

		user.setNgaySinh(ngaysinh);

		Date todayDate = new Date();
		user.setTimeCreateAcc(todayDate);
		user.setName(last_name + " " + first_name);
		user.setUsername(formatName(user.getName()));
		user.setGender(gender);
		user.setPhone(phone);
		user.setEmail(email);

		PasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));
		user.setStatusString("active");

		UserDTO userSaveDto = khachHangService.save(user);

		// Lưu vào firebase và tạo tài khoản authentication
		userSaveDto.setFirstName(first_name);
		userSaveDto.setLastName(last_name);
		userSaveDto.setPasswordRaw(password);

		String uuid = firebaseService.saveUserToFirebase(userSaveDto, true, false, false,false);

		// save uuid to DB
		if (uuid != null) {
			userSaveDto.setFirebaseUid(uuid);

			khachHangService.save(userSaveDto);
		}

		if (userSaveDto != null) {
			KhachHangDTO khachHangDTO = new KhachHangDTO();
			khachHangDTO.setHoTen(user.getName());
			khachHangDTO.setEmail(user.getEmail());
			khachHangDTO.setSoDienThoai(user.getPhone());
			khachHangDTO.setNgaySinh(user.getNgaySinh());
			khachHangDTO.setId(userSaveDto.getId());
			khachHangDTO.setGioiTinh(user.getGender());

			khachHangService.save(khachHangDTO);
		}

		redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
		return "redirect:/dang-nhap";
	}

	public static String formatName(String input) {
		// 1. Chuẩn hóa và loại bỏ dấu tiếng Việt
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
		String noDiacritics = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

		// 2. Tách các từ, viết hoa và nối lại
		String[] parts = noDiacritics.trim().split("\\s+");
		StringBuilder result = new StringBuilder();

		for (String part : parts) {
			if (!part.isEmpty()) {
				// Viết hoa chữ cái đầu mỗi từ, các ký tự còn lại viết thường
				result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1).toLowerCase());
			}
		}

		return result.toString();
	}

}
