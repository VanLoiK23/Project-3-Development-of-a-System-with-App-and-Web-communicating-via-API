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

import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.service.ICategotyService;
import com.thuongmaidientu.service.IColorService;
import com.thuongmaidientu.service.IHedieuhanhService;
import com.thuongmaidientu.service.IKhuVucKhoService;
import com.thuongmaidientu.service.IRamService;
import com.thuongmaidientu.service.IRomService;
import com.thuongmaidientu.service.IXuatxuService;

@RestController(value = "KhoApiOfAdmin")
@RequestMapping("/quan-tri/khu-vuc-kho")
public class KhuVucKhoAPI {
	@Autowired
	private IKhuVucKhoService khoService;

	@DeleteMapping
	public ResponseEntity<String> deleteKho(@RequestParam("id") int id) {
		try {
			khoService.updateTrash(id, "disable");

			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@RequestMapping(value = "/check-kho-name", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkAttributeName(@RequestParam("new") String newValue) {
		Map<String, Object> response = new HashMap<>();
		boolean exists = false;

		exists = khoService.checkNewValue(newValue);

		response.put("exists", exists);

		return response;
	}

	@RequestMapping(value = "/edit_Kho", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> edit_Attribute(@RequestParam("id") Integer id) {
		Map<String, Object> response = new HashMap<>();

		response.put("exists", khoService.findById(id));

		return response;
	}
	
	@GetMapping
	public ResponseEntity<List<KhuVucKhoDTO>> getKho() {
		try {
			List<KhuVucKhoDTO> pDto = khoService.selectAll();
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
