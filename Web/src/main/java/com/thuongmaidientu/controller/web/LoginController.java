package com.thuongmaidientu.controller.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.FirebaseService;
import com.thuongmaidientu.service.IKhachHangService;
import com.thuongmaidientu.until.HashUtil;
import com.thuongmaidientu.until.TimeUtil;
import com.thuongmaidientu.until.TokenUtil;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Controller(value = "LoginController")
public class LoginController {
	@Autowired
	private IKhachHangService khachHangService;

	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Autowired
	private FirebaseService firebaseService;

	@RequestMapping(value = "/dang-nhap", method = RequestMethod.GET)
	public ModelAndView showFormLogin(@ModelAttribute("message") String message, HttpSession session) {
		UserDTO userDTO = (UserDTO) session.getAttribute("user");

		if (userDTO != null) {
			return new ModelAndView("redirect:/trang-chu");
		}

		ModelAndView mav = new ModelAndView("login");

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message);
		}
		return mav;
	}

	@RequestMapping(value = "/dang-nhap", method = RequestMethod.POST)
	public String Login(@RequestParam("name") String name, @RequestParam("password") String password,
			RedirectAttributes redirectAttributes, HttpSession session) {

		UserDTO dto = khachHangService.login(name);

		if (dto == null) {
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy userName!");

			return "redirect:/dang-nhap";

		} else {
			if ("lock".equals(dto.getStatusString())) {
				redirectAttributes.addFlashAttribute("message",
						"Tài khoản đã bị khóa vui lòng liên hệ để được hỗ trợ!");

				return "redirect:/dang-nhap";
			}

			PasswordEncoder encoder = new BCryptPasswordEncoder();

			String hashedPassword = dto.getPassword();

			boolean matches = encoder.matches(password, hashedPassword);

			if (matches) {
				session.setAttribute("user", dto);
				redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
				session.setAttribute("name", dto.getUsername());

				if (dto.getRole().equals("nhân viên")) {
					return "redirect:/quan-tri/Homepage_admin";
				}
				return "redirect:/trang-chu";

			} else {
				redirectAttributes.addFlashAttribute("message", "Mật khẩu không chính xác!");

				return "redirect:/dang-nhap";

			}
		}
	}

	@GetMapping("/send-Password-Reset")
	public ModelAndView showFormFillEmail(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("web/forgotPasswordResetEmail/forgot-password_view");

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message);
		}

		return mav;
	}

	@PostMapping("/send-Password-Reset")
	public String handleSendEmailReset(@RequestParam("email") String email, RedirectAttributes redirectAttributes)
			throws Exception {
		UserDTO userDTO = khachHangService.findByEmail(email);

		if (userDTO == null) {
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy tài khoản !");

			return "redirect:/send-Password-Reset";
		}

		String token = TokenUtil.generateToken();
		String tokenHash = HashUtil.sha256(token);
		Date expiryDate = TimeUtil.getExpiryDate(5); // 5 phút

		System.out.println("Token (for user): " + token); // Gửi user qua link reset
		System.out.println("Hashed Token (save DB): " + tokenHash); // Lưu vào DB
		System.out.println("Expiry time: " + expiryDate);

		userDTO.setResetTokenHash(tokenHash);
		userDTO.setResetTokenExpiresAt(expiryDate);

		khachHangService.save(userDTO);

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("Link reset mật khẩu mới của bạn: ");

			String resetUrl = "http://localhost:8080/Spring-mvc/reset-new-password?token=" + token;

			helper.setText(resetUrl, false); // true = gửi HTML

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("message", "Email khôi phục đã được gửi!");

		return "redirect:/send-Password-Reset";
	}

	@GetMapping("/reset-new-password")
	public ModelAndView resetPassword(@RequestParam(value = "token", required = false) String token,
			@ModelAttribute("message") String message, RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView mav = new ModelAndView("web/forgotPasswordResetEmail/reset_password_view");

		if (token == null) {
			redirectAttributes.addFlashAttribute("message", "Token must not empty !");

			return new ModelAndView("redirect:/send-Password-Reset");
		}

		UserDTO userDTO = khachHangService.findByTokenHash(HashUtil.sha256(token));

		if (userDTO == null) {
			mav.addObject("message", "Token not found.");

			return mav;
		}

		Date expiresAt = userDTO.getResetTokenExpiresAt();
		Date now = new Date();

		if (expiresAt == null || now.after(expiresAt)) {
			mav.addObject("message", "Token has expired.");
			return mav;
		}

		mav.addObject("token", token);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message);
		}

		return mav;
	}

	@PostMapping("/reset-new-password")
	public String handleResetPassword(@RequestParam("token") String token, @RequestParam("password") String password,
			@RequestParam("password_confirmation") String password_confirmation, RedirectAttributes redirectAttributes)
			throws Exception {

		UserDTO userDTO = khachHangService.findByTokenHash(HashUtil.sha256(token));

		if (userDTO == null) {
			redirectAttributes.addFlashAttribute("message", "token not found");

			return "redirect:/reset-new-password?token=" + token;
		}

		Date expiresAt = userDTO.getResetTokenExpiresAt();
		Date now = new Date();

		if (expiresAt == null || now.after(expiresAt)) {
			redirectAttributes.addFlashAttribute("message", "token has expired");
			return "redirect:/reset-new-password?token=" + token;
		}

		if (password.length() < 6) {
			redirectAttributes.addFlashAttribute("message", "Password at least 6 characters");
			return "redirect:/reset-new-password?token=" + token;
		}

		if (!password.equals(password_confirmation)) {
			redirectAttributes.addFlashAttribute("message", "Passwords must match");
			return "redirect:/reset-new-password?token=" + token;
		}

		PasswordEncoder encoder = new BCryptPasswordEncoder();
		userDTO.setPassword(encoder.encode(password));
		userDTO.setResetTokenHash(null);
		userDTO.setResetTokenExpiresAt(null);
		khachHangService.save(userDTO);
		
		userDTO.setPasswordRaw(password);
		
		firebaseService.saveUserToFirebase(userDTO,false,false,true,false);

		redirectAttributes.addFlashAttribute("message", "Password reset succesfull.Now you can login again");

		return "redirect:/dang-nhap";
	}
}
