package com.thuongmaidientu.api.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.dto.RomDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.service.ICategotyService;
import com.thuongmaidientu.service.IColorService;
import com.thuongmaidientu.service.IHedieuhanhService;
import com.thuongmaidientu.service.IRamService;
import com.thuongmaidientu.service.IRomService;
import com.thuongmaidientu.service.IXuatxuService;

@RestController(value = "attributeApiOfAdmin")
@RequestMapping("/quan-tri/thuoc-tinh")
public class AttributeAPI {
	@Autowired
	private ICategotyService categoryService;

	@Autowired
	private IHedieuhanhService hedieuhanhService;

	@Autowired
	private IColorService colorService;

	@Autowired
	private IRamService ramService;

	@Autowired
	private IRomService romService;

	@Autowired
	private IXuatxuService xuatxuService;

	@DeleteMapping
	public ResponseEntity<String> deleteAttribute(@RequestParam("id") int id, @RequestParam("type") String type) {
		try {
			if ("xuatxu".equals(type)) {
				xuatxuService.updateTrash(id, "disable");
			} else if ("hedieuhanh".equals(type)) {
				hedieuhanhService.updateTrash(id, "disable");
			} else if ("thuonghieu".equals(type)) {
				categoryService.updateTrash(id, "disable");
			} else if ("dungluongram".equals(type)) {
				ramService.updateTrash(id, "disable");
			} else if ("dungluongrom".equals(type)) {
				romService.updateTrash(id, "disable");
			} else {
				colorService.updateTrash(id, "disable");
			}
			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@GetMapping
	public ResponseEntity<List<CategoryDTO>> getAllCategory() {
		try {
			List<CategoryDTO> pDto = categoryService.selectAll();
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/ram")
	public ResponseEntity<List<RamDTO>> getRam() {
		try {
			List<RamDTO> pDto = ramService.selectAll();
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/rom")
	public ResponseEntity<List<RomDTO>> getRom() {
		try {
			List<RomDTO> pDto = romService.selectAll();
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/color")
	public ResponseEntity<List<MauSacDTO>> getColor() {
		try {
			List<MauSacDTO> pDto = colorService.selectAll();
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/xuatxu")
	public ResponseEntity<List<XuatXuDTO>> getXX() {
		try {
			List<XuatXuDTO> pDto = xuatxuService.selectAll();
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/hedieuhanh")
	public ResponseEntity<List<HedieuhanhDTO>> getHDH() {
		try {
			List<HedieuhanhDTO> pDto = hedieuhanhService.selectAll();
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping
	public ResponseEntity<CategoryDTO> upsertCategory(@RequestBody CategoryDTO categoryDTO) {
		try {
			CategoryDTO pDto = categoryService.save(categoryDTO);
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(value = "/check-attribute-name", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkAttributeName(@RequestParam("new") String newValue,
			@RequestParam("type") String type) {
		Map<String, Object> response = new HashMap<>();
		boolean exists = false;
		if ("xuatxu".equals(type)) {
			exists = xuatxuService.checkNewValue(newValue);
		} else if ("hedieuhanh".equals(type)) {
			exists = hedieuhanhService.checkNewValue(newValue);
		} else if ("thuonghieu".equals(type)) {
			exists = categoryService.checkNewValue(newValue);
		} else if ("dungluongram".equals(type)) {
			exists = ramService.checkNewValue(Integer.parseInt(newValue));
		} else if ("dungluongrom".equals(type)) {
			exists = romService.checkNewValue(Integer.parseInt(newValue));
		} else {
			exists = colorService.checkNewValue(newValue);
		}

		response.put("exists", exists);

		return response;
	}

	@RequestMapping(value = "/edit_Attribute", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> edit_Attribute(@RequestParam("id") Integer id, @RequestParam("type") String type) {
		Map<String, Object> response = new HashMap<>();
		if ("xuatxu".equals(type)) {
			response.put("exists", xuatxuService.findById(id));
		} else if ("hedieuhanh".equals(type)) {
			response.put("exists", hedieuhanhService.findById(id));
		} else if ("thuonghieu".equals(type)) {
			response.put("exists", categoryService.findById(id));
		} else if ("dungluongram".equals(type)) {
			response.put("exists", ramService.findById(id));
		} else if ("dungluongrom".equals(type)) {
			response.put("exists", romService.findById(id));
		} else {
			response.put("exists", colorService.findById(id));
		}

		return response;
	}
}
