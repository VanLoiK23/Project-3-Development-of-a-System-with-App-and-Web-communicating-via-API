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
import com.thuongmaidientu.service.INhacungcapService;

@RestController(value = "NhacungcapApiOfAdmin")
@RequestMapping("/quan-tri/nha-cung-cap")
public class NhacungcapAPI {
	
	@Autowired
	private INhacungcapService nhacungcapService;
	
	@GetMapping
    public ResponseEntity<List<NhaCungCapDTO>> showListImport() {
        try {
            List<NhaCungCapDTO> pDto = nhacungcapService.selectAll();
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
	public ResponseEntity<String> deleteNCC(@RequestParam("id") int id) {
		try {
			nhacungcapService.updateTrash(id, "disable");

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@RequestMapping(value = "/check-NCC-name", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkAttributeName(@RequestParam("new") String newValue) {
		Map<String, Object> response = new HashMap<>();
		boolean exists = false;

		exists = nhacungcapService.checkNewValue(newValue);

		response.put("exists", exists);

		return response;
	}

	@RequestMapping(value = "/edit_NCC", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> edit_Attribute(@RequestParam("id") Integer id) {
		Map<String, Object> response = new HashMap<>();

		response.put("exists", nhacungcapService.findById(id));

		return response;
	}

}
