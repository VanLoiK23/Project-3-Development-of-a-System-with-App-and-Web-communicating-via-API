package com.thuongmaidientu.api.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.DiscountDTO;
import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.service.IDiscountService;
import com.thuongmaidientu.service.INhacungcapService;

@RestController(value = "DiscountApiOfAdmin")
@RequestMapping("/quan-tri/ma-giam-gia")
public class DiscountAPI {
	
	@Autowired
	private IDiscountService discountService;
	
	@GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDiscount(@RequestParam("id") int id) {
        try {
            List<DiscountDTO> pDto = discountService.selectAll();
            if (pDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
            }
            return ResponseEntity.ok(pDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@DeleteMapping
	public ResponseEntity<String> deleteDiscount(@RequestParam("id") int id) {
		try {
			discountService.updateTrash(id, "disable");

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@RequestMapping(value = "/check-Discount-name", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkAttributeName(@RequestParam("new") String newValue) {
		Map<String, Object> response = new HashMap<>();
		boolean exists = false;

		exists = discountService.checkNewCode(newValue);

		response.put("exists", exists);

		return response;
	}

	@RequestMapping(value = "/edit_Discount", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> edit_Discount(@RequestParam("id") Integer id) {
		Map<String, Object> response = new HashMap<>();

		response.put("exists", discountService.findById(id));

		return response;
	}

}
