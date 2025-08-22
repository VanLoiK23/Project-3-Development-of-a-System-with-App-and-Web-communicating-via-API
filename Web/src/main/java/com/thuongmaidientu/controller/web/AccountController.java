package com.thuongmaidientu.controller.web;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.firebase.auth.FirebaseAuthException;
import com.thuongmaidientu.dto.ChiTietPhieuXuatDTO;
import com.thuongmaidientu.dto.DiscountDTO;
import com.thuongmaidientu.dto.KhachHangDTO;
import com.thuongmaidientu.dto.PhieuXuatDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.ThongTinGiaoHangDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.FirebaseService;
import com.thuongmaidientu.service.IChiTietPXService;
import com.thuongmaidientu.service.ICommentAndRateService;
import com.thuongmaidientu.service.IDiscountService;
import com.thuongmaidientu.service.IKhachHangService;
import com.thuongmaidientu.service.IPhieuXuatService;
import com.thuongmaidientu.service.IProductService;
import com.thuongmaidientu.service.IThongTinGiaoHangService;

import jakarta.servlet.http.HttpSession;

@Controller("accountOfWebController")
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private IKhachHangService khachHangService;

	@Autowired
	private IProductService productService;

	@Autowired
	private ICommentAndRateService commentAndRateService;

	@Autowired
	private IPhieuXuatService phieuXuatService;

	@Autowired
	private IChiTietPXService chiTietPXService;

	@Autowired
	private IThongTinGiaoHangService thongTinGiaoHangService;

	@Autowired
	private IDiscountService discountService;
	
	@Autowired
	private FirebaseService firebaseService;

	@GetMapping
	public ModelAndView showAccountView(HttpSession session, @ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("web/account/quanli_account_view");

		UserDTO userDTO = (UserDTO) session.getAttribute("user");

		if (userDTO == null) {
			return new ModelAndView("redirect:/trang-chu");
		}

		List<PhieuXuatDTO> phieuXuatDTOAllList = phieuXuatService.getAllOrderByUserID(userDTO.getId().intValue());

		phieuXuatDTOAllList.forEach(px -> {
			List<ChiTietPhieuXuatDTO> chiTietPhieuXuatDTOs = chiTietPXService.getListCTPX(px.getId().intValue());

			List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
			chiTietPhieuXuatDTOs.forEach(ctpx -> {
				ProductDTO productDTO = productService.findInfoProductForOrderInWeb(ctpx.getPhienBanSanPhamXuatId());
				productDTO.setMaPX(px.getId().intValue());
				productDTO.setIsCommentWithOrder(
						(commentAndRateService.getExistCommentByMaspAndOrderId(productDTO.getMap().intValue(),
								px.getId().intValue(), userDTO.getId().intValue()) != null));
				productDTOs.add(productDTO);
			});

			Map<Integer, ProductDTO> productMap = new LinkedHashMap<>();
			for (ProductDTO p : productDTOs) {
				productMap.putIfAbsent(p.getMap().intValue(), p); // giữ sản phẩm đầu tiên với id đó
			}

			List<ProductDTO> uniqueProductDTOs = new ArrayList<>(productMap.values());
			px.setListProductDTOsForComment(uniqueProductDTOs);

			px.setListctpx(chiTietPhieuXuatDTOs);
			px.setListProductDTOsForDisplay(productDTOs);
		});

		List<PhieuXuatDTO> orderNotDeliverYet = phieuXuatDTOAllList.stream().filter(px -> px.getStatus() != 4)
				.collect(Collectors.toList());

		List<PhieuXuatDTO> orderHasDeliver = phieuXuatDTOAllList.stream().filter(px -> px.getStatus() == 4)
				.collect(Collectors.toList());

		mav.addObject("user", userDTO);
		mav.addObject("orderNotDeliverYet", orderNotDeliverYet);
		mav.addObject("orderHasDeliver", orderHasDeliver);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message);
		}
//		mav.addObject("orderHasDeliver", orderHasDeliver);

		return mav;
	}

	@PostMapping("/updateIn4Account")
	public String updateIn4Account(@RequestParam("id") Integer id,
			@RequestParam(value = "fullname", required = false) String fullname,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "newpassword", required = false) String newpassword,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "ngaysinh", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngaysinh,
			@RequestParam(value = "gender", required = false) String gender, RedirectAttributes redirectAttributes,HttpSession session) throws FirebaseAuthException, ExecutionException, InterruptedException {

		UserDTO userDTO = khachHangService.findUserDTOById(id);

		if (newpassword != null && !newpassword.isEmpty()) {
			// Kiểm tra mật khẩu có ít nhất 6 ký tự
			if (newpassword.length() < 6) {
				redirectAttributes.addFlashAttribute("message", "Mật khẩu phải có ít nhất 6 ký tự!");
				return "redirect:/account";
			}
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			userDTO.setPassword(encoder.encode(newpassword));

			UserDTO userSaveChangedPass=khachHangService.save(userDTO);
			userSaveChangedPass.setPasswordRaw(newpassword);
			
			firebaseService.saveUserToFirebase(userSaveChangedPass,false,false,true,false);


			redirectAttributes.addFlashAttribute("message", "Mật khẩu đổi mới thành công!");
			return "redirect:/account";
		}

		KhachHangDTO khachHangDTO = khachHangService.findById(id);

		// Kiểm tra số điện thoại phải có đúng 10 chữ số
		if (!phone.matches("\\d{10}")) {
			redirectAttributes.addFlashAttribute("message", "Số điện thoại phải gồm đúng 10 chữ số!");
			return "redirect:/account";
		}

		// Kiểm tra định dạng email đơn giản
		if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
			redirectAttributes.addFlashAttribute("message", "Email không hợp lệ!");
			return "redirect:/account";
		}

		String firstName = getFirstName(fullname);
		String lastName = getLastName(fullname);

		userDTO.setNgaySinh(ngaysinh);
		userDTO.setName(fullname);
		userDTO.setUsername(formatName(fullname));
		userDTO.setGender(gender);
		userDTO.setPhone(phone);
		userDTO.setEmail(email);

		UserDTO userSaveDto = khachHangService.save(userDTO);
		userSaveDto.setFirstName(firstName);
		userSaveDto.setLastName(lastName);
		
		firebaseService.saveUserToFirebase(userSaveDto,false,true,false,false);


		if (userSaveDto != null) {
			khachHangDTO.setHoTen(fullname);
			khachHangDTO.setEmail(email);
			khachHangDTO.setSoDienThoai(phone);
			khachHangDTO.setNgaySinh(ngaysinh);
			khachHangDTO.setGioiTinh(gender);

			khachHangService.save(khachHangDTO);

		}
				
		session.setAttribute("user", khachHangService.findUserDTOById(userSaveDto.getId().intValue()));
		System.out.println(userSaveDto);

		redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin tài khoản thành công!");
		return "redirect:/account";
	}

	@GetMapping("/huydonhang/{id}")
	public String cancleOrder(@PathVariable("id") Integer id, @RequestParam("reason") String reason,
			RedirectAttributes redirectAttributes) {

		phieuXuatService.updateStatusCancel(id, reason);

		redirectAttributes.addFlashAttribute("message", "Hủy đơn hàng thành công!");

		return "redirect:/account";
	}

	@GetMapping("/donhang/{id}")
	public ModelAndView showDetailOrder(@PathVariable("id") Integer id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		ModelAndView mav = new ModelAndView("web/account/chitietdonhang");

		UserDTO userDTO = (UserDTO) session.getAttribute("user");

		if (userDTO == null) {
			return new ModelAndView("redirect:/trang-chu");
		}

		PhieuXuatDTO phieuXuatFindByIDto = phieuXuatService.findById(id);

		if (phieuXuatFindByIDto == null) {
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy đơn hàng!");

			return new ModelAndView("redirect:/account");
		}
		
		if(phieuXuatFindByIDto.getMakh()!=userDTO.getId().intValue()) {
			redirectAttributes.addFlashAttribute("message", "Không tìm thấy đơn hàng!");

			return new ModelAndView("redirect:/account");
		}

		List<ChiTietPhieuXuatDTO> chiTietPhieuXuatDTOs = chiTietPXService
				.getListCTPX(phieuXuatFindByIDto.getId().intValue());

		List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();

		Integer quantity = 0;
		for (ChiTietPhieuXuatDTO ctpx : chiTietPhieuXuatDTOs) {
			quantity+=ctpx.getSoLuong();
			ProductDTO productDTO = productService.findInfoProductForOrderInWeb(ctpx.getPhienBanSanPhamXuatId());
			productDTO.setMaPX(phieuXuatFindByIDto.getId().intValue());
			productDTO.setIsCommentWithOrder(
					(commentAndRateService.getExistCommentByMaspAndOrderId(productDTO.getMap().intValue(),
							phieuXuatFindByIDto.getId().intValue(), userDTO.getId().intValue()) != null));
			productDTOs.add(productDTO);
		}
//		chiTietPhieuXuatDTOs.forEach(ctpx -> {
//			quantity+=ctpx.getSoLuong();
//			ProductDTO productDTO = productService.findInfoProductForOrderInWeb(ctpx.getPhienBanSanPhamXuatId());
//			productDTO.setMaPX(phieuXuatFindByIDto.getId().intValue());
//			productDTO.setIsCommentWithOrder(
//					(commentAndRateService.getExistCommentByMaspAndOrderId(productDTO.getMap().intValue(),
//							phieuXuatFindByIDto.getId().intValue(), userDTO.getId().intValue()) != null));
//			productDTOs.add(productDTO);
//		});

		phieuXuatFindByIDto.setListctpx(chiTietPhieuXuatDTOs);
		phieuXuatFindByIDto.setListProductDTOsForDisplay(productDTOs);

		ThongTinGiaoHangDTO thongTinGiaoHangDTO = thongTinGiaoHangService
				.findById(phieuXuatFindByIDto.getCartShipping());
		if (phieuXuatFindByIDto.getDiscountCode() != null) {

			DiscountDTO discountDTO = discountService.findById(phieuXuatFindByIDto.getDiscountCode());

			phieuXuatFindByIDto.setAmountDiscount(discountDTO.getDiscountAmount());
			phieuXuatFindByIDto.setDiscountCodeRaw(discountDTO.getCode());
		}

		mav.addObject("user", userDTO);
		mav.addObject("quantity",quantity);
		mav.addObject("shipping", thongTinGiaoHangDTO);
		mav.addObject("order", phieuXuatFindByIDto);

		return mav;
	}

	// split firstName and lastName
	public static String getFirstName(String fullName) {
		if (fullName == null || fullName.trim().isEmpty())
			return "";
		String[] parts = fullName.trim().split("\\s+");
		return parts[parts.length - 1]; // phần cuối là tên
	}

	public static String getLastName(String fullName) {
		if (fullName == null || fullName.trim().isEmpty())
			return "";
		String[] parts = fullName.trim().split("\\s+");
		if (parts.length == 1)
			return "";

		// Nối các phần còn lại thành họ + đệm
		StringBuilder lastName = new StringBuilder();
		for (int i = 0; i < parts.length - 1; i++) {
			lastName.append(parts[i]);
			if (i < parts.length - 2) {
				lastName.append(" ");
			}
		}
		return lastName.toString();
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
