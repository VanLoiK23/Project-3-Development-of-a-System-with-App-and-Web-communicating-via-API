package com.thuongmaidientu.controller.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thuongmaidientu.dto.CartDTO;
import com.thuongmaidientu.dto.CartItemDTO;
import com.thuongmaidientu.dto.ChiTietPhieuXuatDTO;
import com.thuongmaidientu.dto.DiscountDTO;
import com.thuongmaidientu.dto.MomoDTO;
import com.thuongmaidientu.dto.OrderItem;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.PhieuXuatDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.ThongTinGiaoHangDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.EmailService;
import com.thuongmaidientu.service.FirebaseService;
import com.thuongmaidientu.service.ICartService;
import com.thuongmaidientu.service.IChiTietPNService;
import com.thuongmaidientu.service.IChiTietPXService;
import com.thuongmaidientu.service.IChiTietSPService;
import com.thuongmaidientu.service.IDiscountService;
import com.thuongmaidientu.service.IMomoService;
import com.thuongmaidientu.service.IPhienbanspService;
import com.thuongmaidientu.service.IPhieuXuatService;
import com.thuongmaidientu.service.IProductService;
import com.thuongmaidientu.service.IThongTinGiaoHangService;

import jakarta.servlet.http.HttpSession;

@Controller("cartOfWebController")
@RequestMapping("/Cart")
public class CartController {
	@Autowired
	private ICartService cartService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IPhienbanspService phienbanspService;

	@Autowired
	private IThongTinGiaoHangService thongTinGiaoHangService;

	@Autowired
	private IDiscountService discountService;

	@Autowired
	private IMomoService momoService;

	@Autowired
	private IPhieuXuatService phieuXuatService;

	@Autowired
	private IChiTietPXService chiTietPXService;

	@Autowired
	private IChiTietSPService chiTietSPService;

	@Autowired
	private IChiTietPNService chiTietPNService;

	@Autowired
	private FirebaseService firebaseService;

	@Autowired
	private EmailService emailService;
	@GetMapping
	public ModelAndView Cart(HttpSession session) {
		ModelAndView mav = new ModelAndView("web/cart/Cart");

		UserDTO userDTO = (UserDTO) session.getAttribute("user");
		CartItemDTO cartItemDTO = new CartItemDTO();

		if (userDTO != null) {
			session.removeAttribute("purchaseWithoutCart");

			cartItemDTO.setListResult(cartService.getCartItemDTOsByClientId(userDTO.getId().intValue()));

			mav.addObject("lenghtCart", cartItemDTO.getListResult().size());

		}

		mav.addObject("cartModel", cartItemDTO);
		mav.addObject("user", userDTO);

		return mav;
	}

	@DeleteMapping
	@ResponseBody
	public ResponseEntity<?> deleteCartItem(HttpSession session, @RequestParam("maphienbansp") Integer maPb) {
		try {
			Map<String, Boolean> mapResultMap = new HashMap<String, Boolean>();
			UserDTO userDTO = (UserDTO) session.getAttribute("user");

			if (userDTO != null) {
				mapResultMap.put("result",
						cartService.deleteByPhienbansanphamAndClientId(maPb, userDTO.getId().intValue()));

				
				//delete cartItem in Firebase
				Integer idCartExist = cartService.getIdCartActiveByClient(userDTO.getId().intValue());

				firebaseService.deleteCartItemByMaphienbansp(idCartExist, maPb);
			} else {
				mapResultMap.put("result", false);
			}

			return ResponseEntity.ok(mapResultMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> updateCartItem(HttpSession session, @RequestParam("maphienbansp") Integer maPb,
			@RequestParam("new_quantity") Integer newQuantity) {
		try {
			Map<String, String> mapResultMap = new HashMap<String, String>();
			UserDTO userDTO = (UserDTO) session.getAttribute("user");

			ProductDTO productDTO = productService.getProductByIdVersionProduct(maPb);

			PhienBanSanPhamDTO phienBanSanPhamDTO = phienbanspService.findById(maPb);

			if (productDTO.getStatus() != 1) {
				mapResultMap.put("result", "Sản phẩm đã ngừng kinh doanh");

				mapResultMap.put("resultReplace", "Bạn có muốn xóa sản phẩm này khỏi giỏ hàng.");

//				cartService.deleteByPhienbansanphamAndClientId(maPb, userDTO.getId().intValue());

				return ResponseEntity.ok(mapResultMap);
			}

			Integer numberInCart = cartService.getQuantityExistInCartByIdVersionAndClientId(maPb,
					userDTO.getId().intValue());
			Integer numberInStock = phienBanSanPhamDTO.getSoLuongTon();

			if (newQuantity + numberInCart == 0) {
				mapResultMap.put("result", "Bạn có muốn xóa sản phẩm này khỏi giỏ hàng.");
			} else if (numberInStock >= (numberInCart + newQuantity)) {
				CartItemDTO cartItemDTO = cartService.getCartItemDTOsByClientIdAndIdVersion(userDTO.getId().intValue(),
						maPb);
				Integer idCartInteger = cartService.getIdCartActiveByClient(userDTO.getId().intValue());

				cartItemDTO.setCart_id(idCartInteger);
				cartItemDTO.setMaphienbansp(maPb);
				cartItemDTO.setSoluong(newQuantity + numberInCart);

//				cartService.updateCartItem(idCartInteger, cartItemDTO);

				cartService.updateExistProductInCartItem(maPb, userDTO.getId().intValue(), cartItemDTO.getSoluong());

				// after update then select and update in fire base :))
				// update cart in firebase
				CartItemDTO cartItemFindById = cartService
						.getCartItemDTOsByClientIdAndIdVersion(userDTO.getId().intValue(), maPb);
//				firebaseService.insertCartSynchFromMySql(cartService.getCartById(idCartIsExist));
				cartItemFindById.setSoluong(newQuantity);
				firebaseService.insertCartItem(cartItemFindById.getCart_id(), cartItemFindById);

				mapResultMap.put("result", "Cập nhật giỏ hàng thành công.");

			} else {
				mapResultMap.put("result", "Sản phẩm không đủ số lượng để mua hàng.");
			}

			return ResponseEntity.ok(mapResultMap);
		} catch (Exception e) {
			e.printStackTrace(); // ✅ In ra lỗi đầy đủ
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

	@PostMapping("/checkValid")
	@ResponseBody
	public ResponseEntity<?> checkValid(HttpSession session) {
		try {
			Map<String, Object> mapResultMap = new HashMap<String, Object>();
			UserDTO userDTO = (UserDTO) session.getAttribute("user");

			Boolean success = true;

			List<CartItemDTO> cartItemDTOs = cartService.getCartItemDTOsByClientId(userDTO.getId().intValue());

			for (CartItemDTO cartItemDTO : cartItemDTOs) {
				ProductDTO productDTO = productService.findById(cartItemDTO.getMasp());
				if (productDTO.getStatus() != 1) {
					mapResultMap.put("message", "❌ Sản phẩm " + productDTO.getTenSanPham() + " đã ngừng kinh doanh");
					success = false;
					break;
				}

				PhienBanSanPhamDTO phienBanSanPhamDTO = phienbanspService.findById(cartItemDTO.getMaphienbansp());

				Integer numberInCart = cartItemDTO.getSoluong();
				Integer numberInStock = phienBanSanPhamDTO.getSoLuongTon();

				if (numberInCart > numberInStock) {
					mapResultMap.put("message", "❌ Sản phẩm " + productDTO.getTenSanPham()
							+ " không đủ số lượng để mua hàng (tồn: " + numberInStock + ").");
					success = false;
					break;
				}

			}

			mapResultMap.put("success", success);
			return ResponseEntity.ok(mapResultMap);
		} catch (Exception e) {
			e.printStackTrace(); // ✅ In ra lỗi đầy đủ
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

	@PostMapping("/save-discount")
	@ResponseBody
	public void saveDiscount(@RequestParam("discountId") Integer discountId, HttpSession session) {
		session.setAttribute("discountId", discountId);
	}

	@GetMapping("/checkout")
	public ModelAndView Checkout(HttpSession session
	/*
	 * @RequestParam(value = "discount", required = false) Integer idDiscount
	 */ ) {
		ModelAndView mav = new ModelAndView("web/checkout/Checkout");

		UserDTO userDTO = (UserDTO) session.getAttribute("user");

		if (userDTO == null) {
			return new ModelAndView("redirect:/trang-chu");
		}

		Map<String, String> mapAttribute = (Map<String, String>) session.getAttribute("purchaseWithoutCart");

		ThongTinGiaoHangDTO thongTinGiaoHangDTO = new ThongTinGiaoHangDTO();

		DiscountDTO discountDTO = new DiscountDTO();

		Integer total = 0;
		Integer tax = 0;
		Integer feeTransport = 0;

		if (mapAttribute == null || mapAttribute.isEmpty()) {
			List<CartItemDTO> items = cartService.getCartItemDTOsByClientId(userDTO.getId().intValue());

			if (items != null && !items.isEmpty()) {
				for (CartItemDTO item : items) {
					total += item.getPriceSale() * item.getSoluong();
				}
			}
		} else {
			String totalStr = mapAttribute.get("total");

			if (totalStr != null) {
				total = Integer.parseInt(totalStr);
			}
		}

		if (total == 0) {
			return new ModelAndView("redirect:/trang-chu");
		}

		List<ThongTinGiaoHangDTO> thongTinGiaoHangDTOs = new ArrayList<ThongTinGiaoHangDTO>();

		// for new customer haven't purchase yet
		thongTinGiaoHangDTOs = thongTinGiaoHangService.getAllByIdkh(userDTO.getId().intValue());

		if (thongTinGiaoHangDTOs != null && !thongTinGiaoHangDTOs.isEmpty()) {
			thongTinGiaoHangDTO = thongTinGiaoHangDTOs.get(0);
		} else {
			thongTinGiaoHangDTO.setHoVaTen(userDTO.getName());
			thongTinGiaoHangDTO.setEmail(userDTO.getEmail());
			thongTinGiaoHangDTO.setSoDienThoai(userDTO.getPhone());

		}

		thongTinGiaoHangDTO.setFirstName(getFirstName(thongTinGiaoHangDTO.getHoVaTen()));
		thongTinGiaoHangDTO.setLastName(getLastName(thongTinGiaoHangDTO.getHoVaTen()));

		if (total < 5_000_000) {
			feeTransport = 200_000;
		}

		tax = total / 10;

		Integer totalFinal = total + tax + feeTransport;
		Integer totalFinalIsConst = totalFinal;

		List<DiscountDTO> discountDTOs = discountService.selectAll();
		Date today = new Date();

		// local variable must be final
		List<DiscountDTO> filterDiscountMeetCondition = discountDTOs
				.stream().filter(discount -> (totalFinalIsConst >= discount.getPaymentLimit())
						&& (discount.getNumberUsed() > 1) && (discount.getExpirationDate().after(today)))
				.collect(Collectors.toList());

		discountDTO.setListResult(filterDiscountMeetCondition);
		mav.addObject("discounts", discountDTO);

		Integer idDiscount = (Integer) session.getAttribute("discountId");

		if (idDiscount != null) {
			DiscountDTO selectDiscountDTO = discountService.findById(idDiscount);

			if (selectDiscountDTO != null) {
				if ((totalFinalIsConst >= selectDiscountDTO.getPaymentLimit())
						&& (selectDiscountDTO.getNumberUsed() > 1)
						&& (selectDiscountDTO.getExpirationDate().after(today))) {
					totalFinal = totalFinal - selectDiscountDTO.getDiscountAmount();
				}
			}
		}

		mav.addObject("idDiscount", idDiscount);
		mav.addObject("totalFinal", totalFinal);
		mav.addObject("feeTransport", feeTransport);
		mav.addObject("tax", tax);
		mav.addObject("infoShipping", thongTinGiaoHangDTO);

		mav.addObject("user", userDTO);

		return mav;
	}

	@PostMapping("/handlePay")
	public String handlePayment(@RequestParam("city") String city, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("district") String district,
			@RequestParam("street") String street, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam("note") String note,
			@RequestParam(value = "ship", required = false) Integer shipId,
			@RequestParam(value = "select", required = false) Integer discountId, @RequestParam("total") Integer total,
			@RequestParam("feeTransport") Integer feeTransport, @RequestParam("tax") Integer tax,
			@RequestParam("urlReturnByMomo") String urlReturnByMomo, HttpSession session) {
		Map<String, Object> mapInfoPaymentUser = new HashMap<String, Object>();

		mapInfoPaymentUser.put("city", city);
		mapInfoPaymentUser.put("firstName", firstName);
		mapInfoPaymentUser.put("lastName", lastName);
		mapInfoPaymentUser.put("district", district);
		mapInfoPaymentUser.put("street", street);
		mapInfoPaymentUser.put("email", email);
		mapInfoPaymentUser.put("phone", phone);
		mapInfoPaymentUser.put("note", note);
		mapInfoPaymentUser.put("total", total);
		mapInfoPaymentUser.put("feeTransport", feeTransport);
		mapInfoPaymentUser.put("tax", tax);
		mapInfoPaymentUser.put("shipId", shipId);
		mapInfoPaymentUser.put("discountId", discountId);

		session.setAttribute("InfoPaymentUser", mapInfoPaymentUser);
		session.removeAttribute("discountId");

		System.out.println("Redirecting to: " + urlReturnByMomo);
		System.out.println("Map to: " + mapInfoPaymentUser);

		return "redirect:" + urlReturnByMomo;

	}

	@GetMapping("/thankyou")
	public ModelAndView handlePyamentSusscess(@RequestParam("partnerCode") String partnerCode,
			@RequestParam("orderId") Long orderId, @RequestParam("amount") Integer amount,
			@RequestParam("orderInfo") String orderInfo, @RequestParam("orderType") String orderType,
			@RequestParam("transId") Long transId, @RequestParam("payType") String payType, HttpSession session) throws ExecutionException, InterruptedException {

		UserDTO userDTO = (UserDTO) session.getAttribute("user");

		if (userDTO == null) {
			return new ModelAndView("redirect:/trang-chu");
		}

		Map<String, Object> mapInfoPaymentUser = (Map<String, Object>) session.getAttribute("InfoPaymentUser");

		if (mapInfoPaymentUser == null || mapInfoPaymentUser.isEmpty()) {
			return new ModelAndView("redirect:/trang-chu");
		}

		Random random = new Random();
		Integer codeCart = random.nextInt(10000);

		MomoDTO momoDTO = new MomoDTO();
		momoDTO.setPartnerCode(partnerCode);
		momoDTO.setOrderId(orderId);
		momoDTO.setAmount(amount.toString());
		momoDTO.setOrderInfo(orderInfo);
		momoDTO.setOrderType(orderType);
		momoDTO.setTransId(transId);
		momoDTO.setPayType(payType);
		momoDTO.setCodeCart(codeCart.toString());

		momoService.insertMomo(momoDTO);

		String city = (String) mapInfoPaymentUser.get("city");
		String firstName = (String) mapInfoPaymentUser.get("firstName");
		String lastName = (String) mapInfoPaymentUser.get("lastName");
		String district = (String) mapInfoPaymentUser.get("district");
		String street = (String) mapInfoPaymentUser.get("street");
		String email = (String) mapInfoPaymentUser.get("email");
		String phone = (String) mapInfoPaymentUser.get("phone");
		String note = (String) mapInfoPaymentUser.get("note");
		Integer totalInt = (Integer) mapInfoPaymentUser.get("total");
		BigDecimal total = BigDecimal.valueOf(totalInt);
		Integer feeTransport = (Integer) mapInfoPaymentUser.get("feeTransport");
		Integer tax = (Integer) mapInfoPaymentUser.get("tax");
		Integer shipId = (Integer) mapInfoPaymentUser.get("shipId");
		Integer discountId = (Integer) mapInfoPaymentUser.get("discountId");

		String fullName = lastName + " " + firstName;

		boolean checkIsSameInfoAddress = false;

		ThongTinGiaoHangDTO thongTinGiaoHangDTO = (shipId != null) ? thongTinGiaoHangService.findById(shipId) : null;

		if (thongTinGiaoHangDTO != null) {
			checkIsSameInfoAddress = (thongTinGiaoHangDTO.getCity().equals(city))
					&& (thongTinGiaoHangDTO.getHoVaTen().equals(fullName))
					&& (thongTinGiaoHangDTO.getDistrict().equals(district))
					&& (thongTinGiaoHangDTO.getStreetName().equals(street))
					&& (thongTinGiaoHangDTO.getEmail().equals(email))
					&& (thongTinGiaoHangDTO.getSoDienThoai().equals(phone));
		} else {
			thongTinGiaoHangDTO = new ThongTinGiaoHangDTO();
		}

		if (!checkIsSameInfoAddress) {
			thongTinGiaoHangDTO = new ThongTinGiaoHangDTO();

			thongTinGiaoHangDTO.setCity(city);
			thongTinGiaoHangDTO.setHoVaTen(fullName);
			thongTinGiaoHangDTO.setDistrict(district);
			thongTinGiaoHangDTO.setStreetName(street);
			thongTinGiaoHangDTO.setEmail(email);
			thongTinGiaoHangDTO.setSoDienThoai(phone);
			thongTinGiaoHangDTO.setIdkh(userDTO.getId().intValue());

		}

		thongTinGiaoHangDTO.setNote(note);
		thongTinGiaoHangDTO.setCountry("Việt Nam");

		ThongTinGiaoHangDTO saveThongTinGiaoHangDTO = thongTinGiaoHangService.insertAddress(thongTinGiaoHangDTO);

		Map<String, String> mapInfoPurchaseWithoutCar = (Map<String, String>) session
				.getAttribute("purchaseWithoutCart");

		CartItemDTO cartItemDTO = new CartItemDTO();

		// Xử lý ko thông qua Cart
		boolean checkIsWithoutCart = mapInfoPurchaseWithoutCar != null && !mapInfoPurchaseWithoutCar.isEmpty();
		if (checkIsWithoutCart) {
			CartItemDTO cartItemInListResult = new CartItemDTO();

			cartItemInListResult.setMaphienbansp(Integer.parseInt(mapInfoPurchaseWithoutCar.get("maphienbansp")));
			cartItemInListResult.setSoluong(Integer.parseInt(mapInfoPurchaseWithoutCar.get("soluong")));
			cartItemInListResult.setPriceSale(Integer.parseInt(mapInfoPurchaseWithoutCar.get("price_sale")));

			cartItemDTO.setListResult(Arrays.asList(cartItemInListResult));
		} else {
			cartItemDTO.setListResult(cartService.getCartItemDTOsByClientId(userDTO.getId().intValue()));
		}

		PhieuXuatDTO phieuXuatDTO = new PhieuXuatDTO();

		phieuXuatDTO.setTongTien(total);
		phieuXuatDTO.setIDKhachHang(userDTO.getId().intValue());
		phieuXuatDTO.setPayment("momo");
		phieuXuatDTO.setCartShipping(saveThongTinGiaoHangDTO.getId().intValue());
		phieuXuatDTO.setDiscountCode(discountId);
		phieuXuatDTO.setFeeTransport((feeTransport > 0) ? 1 : 0);
		phieuXuatDTO.setCodeCart(codeCart.toString());
		phieuXuatDTO.setStatus(0);

		if (discountId != null) {
			discountService.decrese(discountId);
		}

		PhieuXuatDTO savePhieuXuatDTO = phieuXuatService.save(phieuXuatDTO);

		List<Integer> list_mapbspIntegers = new ArrayList<Integer>();

		if (savePhieuXuatDTO != null) {
			// make status is completed if using car
			if (!checkIsWithoutCart) {
				CartDTO cartDTO = cartService.getCartById(cartItemDTO.getListResult().get(0).getCart_id());

				cartDTO.setStatus("completed");
				
				//completed in firebase
				firebaseService.updateCartCompleted(cartDTO.getStatus(), cartDTO.getId().toString());

				cartService.updateCart(cartDTO);
			}

			
			List<OrderItem> orderItems=new ArrayList<OrderItem>();
			for (CartItemDTO item : cartItemDTO.getListResult()) {
				int ma_pbsp = item.getMaphienbansp();
				int quantity = item.getSoluong();
				int priceSale = item.getPriceSale();
				int currentPrice = quantity * priceSale;

				list_mapbspIntegers.add(ma_pbsp);

				ChiTietPhieuXuatDTO chiTietPhieuXuatDTO = new ChiTietPhieuXuatDTO();
				chiTietPhieuXuatDTO.setCodeCart(codeCart.toString());
				chiTietPhieuXuatDTO.setPhieuXuatId(savePhieuXuatDTO.getId().intValue());
				chiTietPhieuXuatDTO.setPhienBanSanPhamXuatId(ma_pbsp);
				chiTietPhieuXuatDTO.setDonGia(priceSale);
				chiTietPhieuXuatDTO.setSoLuong(quantity);

				chiTietPXService.saveCTPX(chiTietPhieuXuatDTO);
				
				//for send email
				OrderItem orderItem=new OrderItem();
				orderItem.setPrice(priceSale);
				orderItem.setQuantity(quantity);
				orderItem.setProductName(productService.findInfoProductForOrderInWeb(ma_pbsp).getTenSanPham());
				
				orderItems.add(orderItem);

				for (Long imei : chiTietSPService.selectListImei(ma_pbsp, PageRequest.of(0, quantity))) {
					chiTietSPService.updateCTSP(savePhieuXuatDTO.getId().intValue(), ma_pbsp, imei);
				}

				Integer slInteger = chiTietPXService.countNumberProductXuatANDMaPBSP(ma_pbsp,
						savePhieuXuatDTO.getId().intValue());
				phienbanspService.updateSL(ma_pbsp, (-slInteger));
			}
			Map<Integer, List<Integer>> mapMaspToMapbspList = new HashMap<>();

			for (Integer i : list_mapbspIntegers) {
				Integer productID = phienbanspService.findMaSP(i);

				// Nếu productID chưa có trong map thì tạo list mới
				mapMaspToMapbspList.computeIfAbsent(productID, k -> new ArrayList<>()).add(i);
			}

			for (Map.Entry<Integer, List<Integer>> entry : mapMaspToMapbspList.entrySet()) {
				Integer masp = entry.getKey();
				List<Integer> mapbspList = entry.getValue();

//				int soluongnhap = 0;
//				int soluongban = 0;
//
//				if (masp != null) {
////					for (int i : mapbspList) {
////						soluongnhap += chiTietPNService.countNumberProductNhap(i);
////						soluongban += chiTietPXService.countNumberProductSold(i);
////					}
//
//					for (int i : phienbanspService.selectAllMaPBSPByMaSP(masp)) {
//						soluongnhap += chiTietPNService.countNumberProductNhap(i);
//						soluongban += chiTietPXService.countNumberProductSold(i);
//					}
//				}
//				productService.updateSLProduct(soluongnhap, soluongban, masp);

				Integer quantityInStock = chiTietSPService.getQuantityProductByStatus(masp, 0);
				Integer quantityExport = chiTietSPService.getQuantityProductByStatus(masp, 1);

				Integer quantityImport = quantityExport + quantityInStock;
				productService.updateSLProduct(quantityImport, quantityExport, masp);

			}

			session.removeAttribute("InfoPaymentUser");
			session.removeAttribute("purchaseWithoutCart");
			
			//after completed send email to notify client
			emailService.sendOrderEmail("huynhvanloi956@gmail.com", orderItems, totalInt);
		}

//		mapAttribute.put("maphienbansp", phienBanSanPhamDTO.getMaPhienbansanpham().toString());
//		mapAttribute.put("total", (totalInteger).toString());
//		mapAttribute.put("price_sale", phienBanSanPhamDTO.getPriceSale().toString());
//		mapAttribute.put("soluong", numberPurchase.toString());
//		mapAttribute.put("idClient", userDTO.getId().toString());

		return new ModelAndView("web/checkout/camon");

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

}
