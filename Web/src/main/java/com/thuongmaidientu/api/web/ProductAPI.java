package com.thuongmaidientu.api.web;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.CartDTO;
import com.thuongmaidientu.dto.CartItemDTO;
import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.FirebaseService;
import com.thuongmaidientu.service.ICartService;
import com.thuongmaidientu.service.ICommentAndRateService;
import com.thuongmaidientu.service.IPhienbanspService;
import com.thuongmaidientu.service.IProductService;

import jakarta.servlet.http.HttpSession;

@RestController(value = "productApiOfWeb")
@RequestMapping("/product")
public class ProductAPI {
	@Autowired
	private IProductService productService;

	@Autowired
	private IPhienbanspService phienbanspService;

	@Autowired
	private ICartService cartService;

	@Autowired
	private ICommentAndRateService commentAndRateService;
	
	@Autowired
	private FirebaseService firebaseService;
//	@RequestMapping(value = "/filterComment", produces = "text/html; charset=UTF-8")

	@PostMapping(value = "/filterComment", produces = "text/html; charset=UTF-8")
	public ResponseEntity<String> filterComment(@RequestParam(name = "filter", required = false) Integer rateFilter,
			@RequestParam("idsp") Integer masp) {
		try {
			StringBuilder textBuilder = new StringBuilder();

			List<CommentAndRateDTO> commentByProduct = commentAndRateService.findByProductId(masp);
			if (commentByProduct == null || commentByProduct.isEmpty()) {

				return ResponseEntity.ok().contentType(MediaType.valueOf("text/html; charset=UTF-8"))
						.body("<div class=\"review-item\">\r\n" + "  <div class=\"review-images\">\r\n"
								+ "    <img src=\"https://img.upanh.tv/2024/11/23/download.jpg\" style=\"width:100%;\" alt=\"Review Image\">\r\n"
								+ "  </div>\r\n</div>");
			}

			List<CommentAndRateDTO> commentsByRate = commentAndRateService.findAllByRateAndStatus(rateFilter, null,
					null);

			List<CommentAndRateDTO> filteredComments = commentsByRate.stream()
					.filter(c -> commentByProduct.stream().anyMatch(cm -> cm.getId().equals(c.getId())))
					.collect(Collectors.toList());

			List<CommentAndRateDTO> sortedComments = filteredComments.stream()
					.sorted(Comparator.comparing(CommentAndRateDTO::getNgayDanhGia).reversed()) // mới nhất lên đầu
					.collect(Collectors.toList());

			for (CommentAndRateDTO comment : sortedComments) {
				textBuilder.append("<div class='review-item'>").append("<div class='review-name'>")
						.append(comment.getHoten()).append("</div>").append("<div class='review-rating'>");

				int count = 0;
				for (int i = 0; i < Math.round(comment.getRate()); i++, count++)
					textBuilder.append('★');
				for (int i = 0; i < (5 - count); i++)
					textBuilder.append('☆');

				textBuilder.append("</div>").append("<div class='review-text'>").append(comment.getContent())
						.append("</div>");

				if (!comment.getImg().isEmpty()) {
					textBuilder.append("<div class='review-images'>").append("<img src='").append(comment.getImg())
							.append("' style='max-width:400px;height:200px' alt='Review Image'>").append("</div>");
				}

				if (comment.getFeeback() == 1) {
					textBuilder
							.append("<div class='feedback-container' style='border-left:6px solid blue;width:400px'>")
							.append("<div class='feedback-header'><div class='employee-info'>")
							.append("<span class='employee-name'>").append(comment.getNhanvien()).append("</span>")
							.append("<span class='feedback-time'>Phản hồi lúc: ").append(comment.getNgayphanhoi())
							.append("</span>").append("</div></div>").append("<div class='feedback-content'>")
							.append(comment.getFeedbackContent()).append("</div>").append("</div>");
				}

				textBuilder.append("</div>");
			}
			System.out.println(textBuilder.toString());

			return ResponseEntity.ok().contentType(MediaType.valueOf("text/html; charset=UTF-8"))
					.body(textBuilder.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
					.body("Lỗi xử lý bình luận");
		}
	}

	@PostMapping("/addToCart")
	public ResponseEntity<?> addCart(@RequestParam("masp") Integer masp, @RequestParam("alias") String alias,
			@RequestParam(value = "ram", required = false) Integer ram,
			@RequestParam(value = "rom", required = false) Integer rom,
			@RequestParam(value = "color", required = false) String color, @RequestParam("num") Integer numberPurchase,
			@RequestParam("isWithoutCart") Boolean isWithoutCart, HttpSession session) {
		try {
			Map<String, String> mapResultMap = new HashMap<String, String>();
			PhienBanSanPhamDTO phienBanSanPhamDTO = new PhienBanSanPhamDTO();
			ProductDTO productDTO = productService.findById(masp);
			Integer numberVersionInCar = null;
			Integer quantityInStock = null;

			UserDTO userDTO = (UserDTO) session.getAttribute("user");

			if (productDTO.getStatus() != 1) {
				mapResultMap.put("Result", "Sản phẩm đã ngừng kinh doanh!");

				return ResponseEntity.ok(mapResultMap);
			}

			if (ram == null) {
//				List<PhienBanSanPhamDTO> phienBanSanPhamDTOs = phienbanspService.findAll(masp);

				phienBanSanPhamDTO = (productDTO.getPbspList() != null) ? productDTO.getPbspList().get(0) : null;
			} else {
				for (PhienBanSanPhamDTO item : productDTO.getPbspList()) {
					boolean matchExact = ram != null && rom != null && color != null
							&& item.getRam().equals(ram.toString()) && item.getRom().equals(rom.toString())
							&& item.getColor().trim().equalsIgnoreCase(color.trim());
					if (matchExact) {
						phienBanSanPhamDTO = item;
					}
				}
//				phienBanSanPhamDTO = phienbanspService.findByConfig(masp, ram, rom, color);
			}

//			numberVersionInCar = cartService
//					.getNumberVersionProductInCartClient(phienBanSanPhamDTO.getMaPhienbansanpham().intValue());
			quantityInStock = phienBanSanPhamDTO.getSoLuongTon();

			if (numberPurchase > quantityInStock) {
				mapResultMap.put("Result", "Số lượng sản phẩm còn " + quantityInStock + " không đủ để mua hàng!!");

				return ResponseEntity.ok(mapResultMap);
			}

			if (isWithoutCart) {
				Integer totalInteger = phienBanSanPhamDTO.getPriceSale() * numberPurchase;

				Map<String, String> mapAttribute = new HashMap<String, String>();
				mapAttribute.put("maphienbansp", phienBanSanPhamDTO.getMaPhienbansanpham().toString());
				mapAttribute.put("total", (totalInteger).toString());
				mapAttribute.put("price_sale", phienBanSanPhamDTO.getPriceSale().toString());
				mapAttribute.put("soluong", numberPurchase.toString());
				mapAttribute.put("idClient", userDTO.getId().toString());

				session.setAttribute("purchaseWithoutCart", mapAttribute);
			} else {
				Date today = new Date();
				CartDTO cartDTO = new CartDTO();
				Integer idCartIsExist = cartService.getIdCartActiveByClient(userDTO.getId().intValue());

				if (idCartIsExist == null) {
					cartDTO.setIdkh(userDTO.getId().intValue());

					cartDTO.setUpdatedAt(today);
					cartDTO.setStatus("active");

					cartDTO = cartService.updateCart(cartDTO);

					idCartIsExist = cartDTO.getId();
				}

				CartItemDTO cartItemDTO = new CartItemDTO();
				cartItemDTO.setCart_id(idCartIsExist);
				cartItemDTO.setMaphienbansp(phienBanSanPhamDTO.getMaPhienbansanpham().intValue());
				cartItemDTO.setMasp(productDTO.getId().intValue());
				cartItemDTO.setSoluong(numberPurchase);
				
				//update cart in firebase
				firebaseService.insertCartSynchFromMySql(cartService.getCartById(idCartIsExist));
				firebaseService.insertCartItem(idCartIsExist, cartItemDTO);
				
				Integer existQuantityVersionProduct=cartService.getQuantityExistInCartByIdVersionAndClientId(phienBanSanPhamDTO.getMaPhienbansanpham().intValue(), userDTO.getId().intValue());

				if(existQuantityVersionProduct!=null) {
					cartItemDTO.setSoluong(numberPurchase+existQuantityVersionProduct);
					cartService.updateExistProductInCartItem(phienBanSanPhamDTO.getMaPhienbansanpham().intValue(), userDTO.getId().intValue(), cartItemDTO.getSoluong());
				}else {
					cartService.updateCartItem(idCartIsExist, cartItemDTO);					
				}
			}

			mapResultMap.put("Result", "Sản phẩm đã được thêm vào giỏ hàng!");

			return ResponseEntity.ok(mapResultMap);

		} catch (Exception e) {
			e.printStackTrace(); // ✅ In ra lỗi đầy đủ
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
		}
	}

}
