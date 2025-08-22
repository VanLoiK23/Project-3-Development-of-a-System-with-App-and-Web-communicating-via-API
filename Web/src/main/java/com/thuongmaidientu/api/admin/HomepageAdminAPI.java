package com.thuongmaidientu.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.ThongkeDTO;
import com.thuongmaidientu.service.IPhieuXuatService;
import com.thuongmaidientu.service.IProductService;
import com.thuongmaidientu.service.IThongKeService;

@RestController(value = "HomepageApiOfAdmin")
@RequestMapping("/quan-tri/thong-ke")
public class HomepageAdminAPI {
	@Autowired
	private IPhieuXuatService phieuXuatService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IThongKeService thongKeService;

	
	@GetMapping
	public ResponseEntity<?> getInfoGeneral() {
		try {
			
			ThongkeDTO thongkeDTO=new ThongkeDTO();
			thongkeDTO.setQuantityProduct(thongKeService.getCountProduct());
			thongkeDTO.setQuantityPurchase(thongKeService.getCountPurchase());
			thongkeDTO.setQuantityUser(thongKeService.getCountUser());
			thongkeDTO.setQuantityWareHouse(thongKeService.getCountWareHouse());
			
			return ResponseEntity.ok(thongkeDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/don-hang/week")
	public ResponseEntity<?> getOrderByWeek() {
		try {
			return ResponseEntity.ok(phieuXuatService.getStatisticalFollowWeek());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/don-hang/month")
	public ResponseEntity<?> getOrderByMonth() {
		try {
			return ResponseEntity.ok(phieuXuatService.getStatisticalFollowMonth());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/doanh-thu/week")
	public ResponseEntity<?> getDoanhThuByWeek() {
		try {
			return ResponseEntity.ok(phieuXuatService.getStatisticalDoanhThuFollowWeek());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/doanh-thu/month")
	public ResponseEntity<?> getDoanhThuByMonth() {
		try {
			return ResponseEntity.ok(phieuXuatService.getStatisticalDoanhThuFollowMonth());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/san-pham/top")
	public ResponseEntity<?> getProductSelling() {
		try {
			return ResponseEntity.ok(productService.getTopSellingProduct());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/nhap-xuat/year")
	public ResponseEntity<?> getNhapXuatByYear() {
		try {
			return ResponseEntity.ok(phieuXuatService.getPnPxByMonth());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/nhap-xuat/quarter")
	public ResponseEntity<?> getNhapXuatByQuarter() {
		try {
			return ResponseEntity.ok(phieuXuatService.getPnPxByQuarter());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm"+e);
		}
	}
}
