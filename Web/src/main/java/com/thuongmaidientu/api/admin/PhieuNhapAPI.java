package com.thuongmaidientu.api.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.ChiTietPhieuNhapDTO;
import com.thuongmaidientu.dto.PhieuNhapDTO;
import com.thuongmaidientu.service.IChiTietPNService;
import com.thuongmaidientu.service.IPhieuNhapService;
import com.thuongmaidientu.service.IProductService;

@RestController(value = "phieuNhapApiOfAdmin")
@RequestMapping("/quan-tri/phieu-nhap")
//@CrossOrigin(origins = "*") // Cho phép mọi nguồn truy cập
public class PhieuNhapAPI {
	@Autowired
	private IPhieuNhapService phieuNhapService;
	
	@Autowired
	private IChiTietPNService chiTietPNService;
	
	@Autowired
	private IProductService productService;


	@DeleteMapping
	public ResponseEntity<String> deletePN(@RequestParam("id") int id) {
		try {
			phieuNhapService.updateTrash(id, "disable");
			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/showdetails")
	public ResponseEntity<?> showdetails(@RequestParam("id") int id) {
	    try {
	    	PhieuNhapDTO dto = phieuNhapService.findById(id);

	    	List<ChiTietPhieuNhapDTO> chiTietPhieuNhapDTOs=chiTietPNService.getListCTPN(dto.getId().intValue());
	    	
	    	List<Map<String, Object>> saveObjects = new ArrayList<>();

	    	for (ChiTietPhieuNhapDTO it : chiTietPhieuNhapDTOs) {
	    	    Object[] productData = productService
	    	        .findChiTietPhienBanSanPhamByPhieuNhap(it.getPhienBanSanPhamNhapId(), dto.getId().intValue())
	    	        .get(0);

	    	    List<Long> imeisList = new ArrayList<>();
	    	    for (Object[] result : productService
	    	            .findChiTietPhienBanSanPhamByPhieuNhap(it.getPhienBanSanPhamNhapId(), dto.getId().intValue())) {
	    	        imeisList.add((Long) result[6]); // hoặc: Long.parseLong(result[6].toString())
	    	    }

	    	    Map<String, Object> productInfo = new HashMap<>();
	    	    productInfo.put("info", productData);
	    	    productInfo.put("imeis", imeisList);

	    	    saveObjects.add(productInfo);
	    	}

	    	dto.setProduct_info(saveObjects);

	       
	        return ResponseEntity.ok(dto); 
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy danh sách sản phẩm"+e);
	    }
	}
//
//
//
//	@GetMapping(value = "/show_details_sp", produces = "application/json; charset=UTF-8")
//	public ResponseEntity<ProductDTO> showDetailsSp(@RequestParam("id") int id) {
//		try {
//			ProductDTO pDto = productService.findById(id);
//			if (pDto == null) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//			}
//			return ResponseEntity.ok(pDto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}
//
//	@GetMapping(value = "/import_sp", produces = "application/json; charset=UTF-8")
//	public ResponseEntity<Map<String, Object>> import_sp(@RequestParam("id") int id) {
//		try {
//			ProductDTO pDto = productService.findById(id);
//			if (pDto == null) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//			}
//
//			// Lấy danh sách phiên bản sản phẩm
//			List<PhienBanSanPhamDTO> list_pb = phienbanspService.findAll(id);
//
//			// Đóng gói response thành JSON
//			Map<String, Object> response = new HashMap<>();
//			response.put("details_sp", pDto);
//			response.put("list_pb", list_pb);
//
//			return ResponseEntity.ok(response);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}
//
//	@GetMapping(value = "/show_config_sp", produces = "application/json; charset=UTF-8")
//	public ResponseEntity<String> show_config_sp(@RequestParam("id") int id) {
//		try {
//			List<PhienBanSanPhamDTO> pDto = phienbanspService.findAll(id);
//			if (pDto == null) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//			}
//			String str = "";
//			int i = 0;
//
//			for (PhienBanSanPhamDTO phienBanSanPhamDTO : pDto) {
//				i++;
//				str += "<tr>"; // Mở hàng table
//
//				str += "<td>" + i + "</td>";
//
//				// Kiểm tra RAM
//				if (phienBanSanPhamDTO.getRam() == null) {
//					str += "<td>Không</td>";
//				} else {
//					str += "<td>" + phienBanSanPhamDTO.getRam() + " GB</td>";
//				}
//
//				// Kiểm tra ROM
//				if (phienBanSanPhamDTO.getRom() == null) {
//					str += "<td>Không</td>";
//				} else {
//					str += "<td>" + phienBanSanPhamDTO.getRom() + " GB</td>";
//				}
//
//				// Màu sắc
//				str += "<td>" + phienBanSanPhamDTO.getColor() + "</td>";
//
//				// Giá nhập
//				str += "<td>" + String.format("%,d", phienBanSanPhamDTO.getGiaNhap()) + " ₫</td>";
//
//				// Giá xuất
//				str += "<td>" + String.format("%,d", phienBanSanPhamDTO.getGiaXuat()) + " ₫</td>";
//
//				str += "</tr>"; // Đóng hàng table
//			}
//
//			return ResponseEntity.ok(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}
//
//	@GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
//	public ResponseEntity<List<ProductDTO>> search(@RequestParam("name") String name) {
//		try {
//			List<ProductDTO> pDto = productService.searchLikeName(name);
//			if (pDto == null || pDto.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//			}
//			return ResponseEntity.ok(pDto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//
//	}
//	
//	@RequestMapping(value = "/check-product-name", method = RequestMethod.GET)
//	@ResponseBody
//	public Map<String, Object> checkProductName(@RequestParam("tensp") String tensp) {
//	    Map<String, Object> response = new HashMap<>();
//	    boolean exists = productService.checkExistTenSanPham(tensp); // Kiểm tra tên sản phẩm trong database
//	    
//	    response.put("exists", exists); // Trả về true nếu sản phẩm đã tồn tại, false nếu không
//	    
//	    return response;
//	}


}
