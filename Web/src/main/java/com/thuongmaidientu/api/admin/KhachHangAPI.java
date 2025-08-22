package com.thuongmaidientu.api.admin;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.KhachHangDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.FirebaseService;
import com.thuongmaidientu.service.IKhachHangService;
import com.thuongmaidientu.until.HashUtil;
import com.thuongmaidientu.until.TimeUtil;
import com.thuongmaidientu.until.TokenUtil;

import jakarta.mail.internet.MimeMessage;

@RestController(value = "KhachHangApiOfAdmin")
@RequestMapping("/quan-tri/khach-hang")
public class KhachHangAPI {

	@Autowired
	private IKhachHangService khachHangService;
	
	@Autowired
	private FirebaseService firebaseService;
	
	@Autowired
	private JavaMailSenderImpl mailSender;

	@DeleteMapping
	public ResponseEntity<String> deleteKH(@RequestParam("id") int id, @RequestParam("status") String status) {
		try {
			khachHangService.updateStatus(id, status);

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@PutMapping("/login/{id}/{token}")
	public ResponseEntity<String> loginUpdatetokenFCM(@PathVariable("id") Integer id,
			@PathVariable("token") String token) {
		try {
			khachHangService.saveFCMtoken(id, token);
			return ResponseEntity.ok("Update Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@PostMapping
	public ResponseEntity<?> addKH(@RequestBody UserDTO user) {
		try {
			if (user != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

				try {
					Date birthDate = sdf.parse(user.getBod());

					user.setNgaySinh(birthDate);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				Date todayDate = new Date();
				user.setTimeCreateAcc(todayDate);
				user.setName(user.getLastName() + " " + user.getFirstName());
				user.setUsername(formatName(user.getLastName() + " " + user.getFirstName()));
				user.setGender("male");
				PasswordEncoder encoder = new BCryptPasswordEncoder();

				String passwordRaw = user.getPassword();

				user.setPassword(encoder.encode(passwordRaw));
				System.out.println(user);

				UserDTO userSaveDto = khachHangService.save(user);

				if (userSaveDto != null) {
					KhachHangDTO khachHangDTO = new KhachHangDTO();
					khachHangDTO.setHoTen(user.getLastName() + " " + user.getFirstName());
					khachHangDTO.setEmail(user.getEmail());
					khachHangDTO.setSoDienThoai(user.getPhone());
					khachHangDTO.setNgaySinh(user.getNgaySinh());
					khachHangDTO.setId(userSaveDto.getId());
					khachHangDTO.setGioiTinh(user.getGender());
					System.out.println(userSaveDto);

					khachHangService.save(khachHangDTO);

				}

				return ResponseEntity.ok(userSaveDto);
			} else {
				return ResponseEntity.badRequest().body("Dữ liệu đầu vào không hợp lệ");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi khi thêm khách hàng: " + e.getMessage());
		}
	}

	@PutMapping
	public void updateAllUser(@RequestBody UserDTO User) {
		try {
			if (User != null) {

				User.setName(User.getLastName() + " " + User.getFirstName());
				User.setUsername(formatName(User.getName()));

				System.out.println("Tét " + User);

				khachHangService.updateFromAndroid(User);
				
				UserDTO userDTO=khachHangService.findUserDTOById(User.getId().intValue());
				
				firebaseService.saveUserToFirebase(userDTO,false,true,false,true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllUser() {
		try {

			List<UserDTO> list = khachHangService.selectAll();

			return ResponseEntity.ok(list);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi khi lấy khách hàng: " + e.getMessage());
		}
	}

	@PostMapping("/status/{id}/{status}")
	public void updateStatus(@PathVariable("id") Integer id, @PathVariable("status") String status) {
		try {
			khachHangService.updateStatus(id, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/edit_KH", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> edit_KhachHnag(@RequestParam("id") Integer id) {
		Map<String, Object> response = new HashMap<>();

		response.put("exists", khachHangService.findById(id));

		return response;
	}

	@PostMapping("/send-Password-Reset")
	public ResponseEntity<?> handleSendEmailReset(@RequestBody UserDTO user)
			throws Exception {
		UserDTO userDTO = khachHangService.findByEmail(user.getEmail());

		if (userDTO == null) {
			return ResponseEntity.ok("Not found");
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

			helper.setTo(user.getEmail());
			helper.setSubject("Link reset mật khẩu mới của bạn: ");

			String resetUrl = "http://192.168.1.70:8080/Spring-mvc/reset-new-password?token=" + token;

			helper.setText(resetUrl, false); // true = gửi HTML

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return ResponseEntity.ok("Email khôi phục đã được gửi!");
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
