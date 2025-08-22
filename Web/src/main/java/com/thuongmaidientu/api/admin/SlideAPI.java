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

import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.SlideDTO;
import com.thuongmaidientu.service.INhacungcapService;
import com.thuongmaidientu.service.ISlideService;

@RestController(value = "SlideApiOfAdmin")
@RequestMapping("/quan-tri/banner")
public class SlideAPI {
	
	@Autowired
	private ISlideService slideService;
	
	@GetMapping
    public ResponseEntity<List<SlideDTO>> getAllSlide() {
        try {
            List<SlideDTO> pDto = slideService.selectAll();
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
	public ResponseEntity<String> deleteSlide(@RequestParam("id") int id) {
		try {
			slideService.updateTrash(id, "disable");

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@RequestMapping(value = "/edit_Slide", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> edit_Attribute(@RequestParam("id") Integer id) {
		Map<String, Object> response = new HashMap<>();

		response.put("exists", slideService.findById(id));

		return response;
	}

}
