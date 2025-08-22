package com.thuongmaidientu.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.ThongkeDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.IPhienbanspService;
import com.thuongmaidientu.service.IProductService;

import jakarta.servlet.http.HttpSession;

@RestController(value = "homeApiOfWeb")
@RequestMapping("/homepage")
public class HomepageAPI {
	@Autowired
	private IProductService productService;

	@Autowired
	private IPhienbanspService phienbanspService;

	@PostMapping("/getPrice")
	public ResponseEntity<?> getPrice(@RequestParam("alias") String alias, @RequestParam("id_pbsp") Integer idPBSP) {
		try {

			ProductDTO productDTO = productService.getDetailProductDTOByAlias(alias, idPBSP);
			return ResponseEntity.ok(productDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@GetMapping("/checkLogin")
	public ResponseEntity<?> checkLogin(HttpSession session) {
		try {
			UserDTO userDTO = (UserDTO) session.getAttribute("user");
			Map<String, Boolean> mapResultMap = new HashMap<String, Boolean>();

			mapResultMap.put("loggedIn", userDTO != null);

			return ResponseEntity.ok(mapResultMap);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logOut(HttpSession session) {
		try {
			session.removeAttribute("user");
			session.removeAttribute("name");
			
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@GetMapping("/show_suggest")
	public ResponseEntity<?> showSuggest(@RequestParam("suggest") String suggest) {
		try {

			return ResponseEntity.ok(productService.findByAlphabet(suggest));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@GetMapping("/filter")
	public ResponseEntity<?> filter(@RequestParam("search") String search) {
		try {
			List<ProductDTO> productDTOs = productService.filterProducts(null, null, null, null, null, search, null);

			List<Integer> foundIds = new ArrayList<Integer>();
			if (productDTOs != null) {
				productDTOs.forEach(product -> {
					foundIds.add(product.getId().intValue());
				});
			}
			return ResponseEntity.ok(foundIds);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
}
