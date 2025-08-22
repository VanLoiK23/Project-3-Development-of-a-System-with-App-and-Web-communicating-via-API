package com.thuongmaidientu.api.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.CTSanPhamDTO;
import com.thuongmaidientu.dto.ChiTietPhieuNhapDTO;
import com.thuongmaidientu.dto.ImportProductRequest;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.PhieuNhapDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.ProductVariant;
import com.thuongmaidientu.dto.PromoDTO;
import com.thuongmaidientu.dto.RequestProductInteraction;
import com.thuongmaidientu.service.ICategotyService;
import com.thuongmaidientu.service.IChiTietPNService;
import com.thuongmaidientu.service.IChiTietPXService;
import com.thuongmaidientu.service.IChiTietSPService;
import com.thuongmaidientu.service.IColorService;
import com.thuongmaidientu.service.IHedieuhanhService;
import com.thuongmaidientu.service.IKhuVucKhoService;
import com.thuongmaidientu.service.IPhienbanspService;
import com.thuongmaidientu.service.IPhieuNhapService;
import com.thuongmaidientu.service.IProductService;
import com.thuongmaidientu.service.IRamService;
import com.thuongmaidientu.service.IRomService;
import com.thuongmaidientu.service.IXuatxuService;
import com.thuongmaidientu.service.impl.XuatxuService;

import netscape.javascript.JSObject;

@RestController(value = "productApiOfAdmin")
@RequestMapping("/quan-tri/san-pham")
//@CrossOrigin(origins = "*") // Cho phép mọi nguồn truy cập
public class ProductAPI {
	@Autowired
	private IProductService productService;

	@Autowired
	private IPhienbanspService phienbanspService;

	@Autowired
	private IChiTietPNService pnService;

	@Autowired
	private IChiTietPXService pxService;

	@Autowired
	private IChiTietSPService ctspService;

	@Autowired
	private IPhieuNhapService importService;

	@Autowired
	private IColorService colorService;

	@Autowired
	private IRamService ramService;

	@Autowired
	private IRomService romService;

	@Autowired
	private IXuatxuService xuatxuService;

	@Autowired
	private ICategotyService categotyService;

	@Autowired
	private IKhuVucKhoService khoService;

	@Autowired
	private IHedieuhanhService hedieuhanhService;

	@DeleteMapping
	public ResponseEntity<String> deleteProduct(@RequestParam("id") int id) {
		try {
			productService.updateTrash(id, "disable");
			return ResponseEntity.ok("Delete Success");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		try {
			List<ProductDTO> dtoList = productService.getALlProducts();

			for (ProductDTO dto : dtoList) {
				List<PhienBanSanPhamDTO> versions = phienbanspService.findAll(dto.getId().intValue());
				dto.setPbspList(versions);
			}

			return ResponseEntity.ok(dtoList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy danh sách sản phẩm");
		}
	}

	@GetMapping(value = "/show_details_sp", produces = "application/json; charset=UTF-8")
	public ResponseEntity<ProductDTO> showDetailsSp(@RequestParam("id") int id) {
		try {
			ProductDTO pDto = productService.findById(id);
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(value = "/import_sp", produces = "application/json; charset=UTF-8")
	public ResponseEntity<Map<String, Object>> import_sp(@RequestParam("id") int id) {
		try {
			ProductDTO pDto = productService.findById(id);
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			// Lấy danh sách phiên bản sản phẩm
			List<PhienBanSanPhamDTO> list_pb = phienbanspService.findAll(id);

			// Đóng gói response thành JSON
			Map<String, Object> response = new HashMap<>();
			response.put("details_sp", pDto);
			response.put("list_pb", list_pb);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(value = "/show_config_sp", produces = "application/json; charset=UTF-8")
	public ResponseEntity<String> show_config_sp(@RequestParam("id") int id) {
		try {
			List<PhienBanSanPhamDTO> pDto = phienbanspService.findAll(id);
			if (pDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			String str = "";
			int i = 0;

			for (PhienBanSanPhamDTO phienBanSanPhamDTO : pDto) {
				i++;
				str += "<tr>"; // Mở hàng table

				str += "<td>" + i + "</td>";

				// Kiểm tra RAM
				if (phienBanSanPhamDTO.getRam() == null) {
					str += "<td>Không</td>";
				} else {
					str += "<td>" + phienBanSanPhamDTO.getRam() + " GB</td>";
				}

				// Kiểm tra ROM
				if (phienBanSanPhamDTO.getRom() == null) {
					str += "<td>Không</td>";
				} else {
					str += "<td>" + phienBanSanPhamDTO.getRom() + " GB</td>";
				}

				// Màu sắc
				str += "<td>" + phienBanSanPhamDTO.getColor() + "</td>";

				// Giá nhập
				str += "<td>" + String.format("%,d", phienBanSanPhamDTO.getGiaNhap()) + " ₫</td>";

				// Giá xuất
				str += "<td>" + String.format("%,d", phienBanSanPhamDTO.getGiaXuat()) + " ₫</td>";

				str += "</tr>"; // Đóng hàng table
			}

			return ResponseEntity.ok(str);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
	public ResponseEntity<List<ProductDTO>> search(@RequestParam("name") String name) {
		try {
			List<ProductDTO> pDto = productService.searchLikeName(name);
			if (pDto == null || pDto.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(pDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@RequestMapping(value = "/check-product-name", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> checkProductName(@RequestParam("tensp") String tensp) {
		Map<String, Object> response = new HashMap<>();
		boolean exists = productService.checkExistTenSanPham(tensp); // Kiểm tra tên sản phẩm trong database

		response.put("exists", exists); // Trả về true nếu sản phẩm đã tồn tại, false nếu không

		return response;
	}

	@DeleteMapping("/{id}")
	public void deleteProductPermanent(@PathVariable("id") int id) {
		productService.delete(id);
	}

	@PostMapping("/import")
	public void importSP(@RequestBody ImportProductRequest importProductRequest) {
		System.out.println("Import request: "+importProductRequest);
		
		List<Integer> id = importProductRequest.getIds();
		List<Integer> price = importProductRequest.getPrices();
		List<Integer> number = importProductRequest.getQuantity();
		List<Long> imei = importProductRequest.getImei();
		Integer supplier = importProductRequest.getSupplier().intValue();
		Double total = 0.0;
		for (int i = 0; i < id.size(); i++) {

			int currentPrice = price.get(i);
			int currentNumber = number.get(i);

			total += currentNumber * currentPrice;
		}

//		      $_SESSION['id']

		PhieuNhapDTO saveImportDto = new PhieuNhapDTO();
		saveImportDto.setNguoiTaoPhieuId(1);
		saveImportDto.setNhaCungCapID(supplier);
		saveImportDto.setTongTien(BigDecimal.valueOf(total));

		PhieuNhapDTO dto = importService.save(saveImportDto);
		if (dto != null) {
			for (int i = 0; i < id.size(); i++) {
				int currentId = id.get(i);
				int currentPrice = price.get(i);
				int currentNumber = number.get(i);
				Long currentIMEI = imei.get(i);

				ChiTietPhieuNhapDTO pbpn = new ChiTietPhieuNhapDTO();
				pbpn.setPhieunhapId(dto.getId().intValue());
				pbpn.setDonGia(currentPrice);
				pbpn.setSoLuong(currentNumber);
				pbpn.setPhienBanSanPhamNhapId(currentId);

				pnService.saveCTPN(pbpn);

				for (int j = 0; j < currentNumber; j++) {
					CTSanPhamDTO ctSanPhamDTO = new CTSanPhamDTO();

					ctSanPhamDTO.setMaImei(currentIMEI);
					currentIMEI++;
					ctSanPhamDTO.setPnId(dto.getId().intValue());
					ctSanPhamDTO.setProductVersionId(currentId);

					ctspService.save(ctSanPhamDTO);
				}

				Integer slInteger = pnService.countNumberProductNhapANDMaPBSP(currentId, dto.getId().intValue());
				phienbanspService.updateSL(currentId, slInteger);
			}
		}

		Integer masp = phienbanspService.findMaSP(id.get(0));
//		int soluongnhap = 0;
//		int soluongban = 0;
//
//		if (productID != null) {
////			for (int i : id) {
////				soluongnhap += pnService.countNumberProductNhap(i);
////				soluongban += pxService.countNumberProductSold(i);
////			}
//
//			for (int i : phienbanspService.selectAllMaPBSPByMaSP(productID)) {
//				soluongnhap += pnService.countNumberProductNhap(i);
//				soluongban += pxService.countNumberProductSold(i);
//			}
//		}
//
//		productService.updateSLProduct(soluongnhap, soluongban, productID);
		
		Integer quantityInStock = ctspService.getQuantityProductByStatus(masp, 0);
		Integer quantityExport = ctspService.getQuantityProductByStatus(masp, 1);

		Integer quantityImport = quantityExport + quantityInStock;
		productService.updateSLProduct(quantityImport, quantityExport, masp);

	}

	@PostMapping("/add_sp")
	public void addSanPham(@RequestBody RequestProductInteraction request) {

		System.out.println("DTO: " + request);

		ProductDTO dto = new ProductDTO();

		dto.setHinhAnh(request.getImg());
		dto.setTenSanPham(request.getName());
		dto.setXuatXuId(request.getXuatxu());
		if (request.getHedieuhanh() == -1) {
			request.setHedieuhanh(null);
		}
		dto.setHeDieuHanhId(request.getHedieuhanh());
		dto.setKhuVucKhoId(request.getKhuvuckho());
		dto.setThuongHieuId(request.getThuonghieu());
		dto.setDungLuongPin(request.getDungluongpin());
		dto.setCameraTruoc(request.getCameratruoc());
		dto.setCameraSau(request.getCamerasau());
		dto.setPhienBanHDH(request.getPhienbanHDH().doubleValue());
		dto.setManHinh(request.getManhinh());
		dto.setSortDesc(request.getTitle());
		dto.setDetail(request.getDescription());

		// Xử lý khuyến mãi
		List<PromoDTO> promoList = new ArrayList<>();
		if (request.getIsTragop())
			promoList.add(new PromoDTO("tragop", "0"));
		if (request.getGiare() > 0)
			promoList.add(new PromoDTO("giareonline", String.valueOf(request.getGiare())));
		if (request.getGiamgia() > 0)
			promoList.add(new PromoDTO("giamgia", String.valueOf(request.getGiamgia())));
		promoList.add(new PromoDTO("moiramat", "0"));

		dto.setPromo(dto.convertPromoListToJson(promoList));
		dto.setAlias(dto.getTenSanPham().toLowerCase().replace(" ", "-"));

		// Lưu sản phẩm
		ProductDTO savedProduct = productService.save(dto);
		Long productId = savedProduct.getId();
		List<String> ramList = new ArrayList<>();

		List<String> romList = new ArrayList<>();

		List<String> colorList = new ArrayList<>();

		List<Integer> priceNList = new ArrayList<>();

		List<Integer> priceXList = new ArrayList<>();

		request.getListPB().forEach(it -> {
			String ramValueOnly = null;
			String romValueOnly = null;

			if ("Không".equals(it.getRam())) {
				it.setRam(null);
				it.setRom(null);
			} else {
				ramValueOnly = it.getRam().replaceAll("[^0-9]", "");
				romValueOnly = it.getRom().replaceAll("[^0-9]", "");

				ramList.add(ramValueOnly);
				romList.add(romValueOnly);
			}

			colorList.add(it.getColor());
			priceNList.add(Integer.parseInt(it.getImportPrice()));
			priceXList.add(Integer.parseInt(it.getExportPrice()));
		});

		// Xử lý RAM, ROM, COLOR
		if ((ramList == null || ramList.isEmpty()) && (romList == null || romList.isEmpty())) { // Nếu không có RAM &
																								// ROM, chỉ thêm màu sắc
			saveVariant(productId, null, null, colorList.get(0), priceNList.get(0), priceXList.get(0),
					request.getGiare());
		} else {
			for (int i = 0; i < ramList.size(); i++) {
				saveVariant(productId, ramList.get(i), romList.get(i), colorList.get(i), priceNList.get(i),
						priceXList.get(i), request.getGiare());
			}
		}

	}

	@PutMapping("/edit_sp")
	public void updateSanPham(@RequestBody RequestProductInteraction request) {

		System.out.println("DTO: " + request);

		ProductDTO dto = productService.findById(request.getId());

		dto.setHinhAnh(request.getImg());
		dto.setTenSanPham(request.getName());
		dto.setXuatXuId(request.getXuatxu());
		if (request.getHedieuhanh() == -1) {
			request.setHedieuhanh(null);
		}
		dto.setHeDieuHanhId(request.getHedieuhanh());
		dto.setKhuVucKhoId(request.getKhuvuckho());
		dto.setThuongHieuId(request.getThuonghieu());
		dto.setDungLuongPin(request.getDungluongpin());
		dto.setCameraTruoc(request.getCameratruoc());
		dto.setCameraSau(request.getCamerasau());
		dto.setPhienBanHDH(request.getPhienbanHDH().doubleValue());
		dto.setManHinh(request.getManhinh());
		dto.setSortDesc(request.getTitle());
		dto.setDetail(request.getDescription());
		dto.setId(request.getId().longValue());

		// Xử lý khuyến mãi
		List<PromoDTO> promoList = new ArrayList<>();
		if (request.getIsTragop())
			promoList.add(new PromoDTO("tragop", "0"));
		if (request.getGiare() > 0)
			promoList.add(new PromoDTO("giareonline", String.valueOf(request.getGiare())));
		if (request.getGiamgia() > 0)
			promoList.add(new PromoDTO("giamgia", String.valueOf(request.getGiamgia())));

		dto.setPromo(dto.convertPromoListToJson(promoList));
		dto.setAlias(dto.getTenSanPham().toLowerCase().replace(" ", "-"));

		// Lưu sản phẩm
		ProductDTO savedProduct = productService.save(dto);

		List<String> ramList = new ArrayList<>();

		List<String> romList = new ArrayList<>();

		List<String> colorList = new ArrayList<>();

		List<Integer> priceNList = new ArrayList<>();

		List<Integer> priceXList = new ArrayList<>();

		List<Long> existingVariantIds = new ArrayList<>();

		request.getListPB().forEach(it -> {
			String ramValueOnly = null;
			String romValueOnly = null;

			if ("Không".equals(it.getRam())) {
				it.setRam(null);
				it.setRom(null);
			} else {
				ramValueOnly = it.getRam().replaceAll("[^0-9]", "");
				romValueOnly = it.getRom().replaceAll("[^0-9]", "");

				ramList.add(ramValueOnly);
				romList.add(romValueOnly);
			}

			colorList.add(it.getColor());
			priceNList.add(Integer.parseInt(it.getImportPrice()));
			priceXList.add(Integer.parseInt(it.getExportPrice()));

			if (it.getExistingVariantIds() != null) {
				existingVariantIds.add(it.getExistingVariantIds().longValue());
			}
		});

		// Xử lý RAM, ROM, COLOR
		if ((ramList == null || ramList.isEmpty()) && (romList == null || romList.isEmpty())) {

			Integer colorId = colorService.getIdByValue(colorList.get(0));

			int normalPrice = priceNList.get(0);
			int salePrice = priceXList.get(0);
			Float salePercentage = (float) (request.getGiare() / 100);
			Integer finalPrice = (int) (salePrice - salePrice * (request.getGiare() / 100));

			PhienBanSanPhamDTO variant = new PhienBanSanPhamDTO(dto.getId(), null, null, colorId, normalPrice,
					salePrice, salePercentage, finalPrice);

			Long variantId;
			if (!existingVariantIds.isEmpty()) {
				variantId = existingVariantIds.get(0);
			} else {
				variantId = phienbanspService.findVariantId(variant, dto.getId());
			}

			if (variantId == null || variantId == 0) {
				// Nếu không tìm thấy, thêm mới
				phienbanspService.save(variant);
			} else {
				variant.setMaPhienbansanpham(variantId);
				// Nếu tồn tại, cập nhật dữ liệu
				phienbanspService.update(variant);
			}
		} else {

			List<Integer> handledVariantIds = new ArrayList<>();

			List<Integer> allVariantIds = phienbanspService
					.selectAllMaPBSPByMaSP(dto.getId() != null ? dto.getId().intValue() : null);

			for (int i = 0; i < ramList.size(); i++) {
				Integer ramId = ramService.getIdByValue(Integer.parseInt(ramList.get(i)));
				Integer romId = romService.getIdByValue(Integer.parseInt(romList.get(i)));
				Integer colorId = colorService.getIdByValue(colorList.get(i));

				int normalPrice = priceNList.get(i);
				int salePrice = priceXList.get(i);
				Float salePercentage = (float) (request.getGiare() / 100);
				Integer finalPrice = (int) (salePrice - salePrice * (request.getGiare() / 100));

				PhienBanSanPhamDTO variant = new PhienBanSanPhamDTO(dto.getId(), romId, ramId, colorId, normalPrice,
						salePrice, salePercentage, finalPrice);

				Long variantId;
				if (!existingVariantIds.isEmpty() && i < existingVariantIds.size()) {
					variantId = existingVariantIds.get(i);
				} else {
					variantId = phienbanspService.findVariantId(variant, dto.getId());
				}

				if (variantId == null || variantId == 0) {
					// Nếu không tìm thấy, thêm mới
					PhienBanSanPhamDTO newVariantId = phienbanspService.save(variant);
					handledVariantIds.add(newVariantId.getMaPhienbansanpham().intValue());
				} else {
					variant.setMaPhienbansanpham(variantId);
					// Nếu tồn tại, cập nhật dữ liệu
					phienbanspService.update(variant);
					handledVariantIds.add(variantId.intValue());

				}
			}

			// Xóa các phiên bản không được xử lý
			List<Integer> idsToDelete = allVariantIds.stream().filter(id -> !handledVariantIds.contains(id))
					.collect(Collectors.toList());

			for (Integer id : idsToDelete) {

				if (!handledVariantIds.contains(id)) {
					Long i = Long.valueOf(id);

					pnService.delete(i);
					pxService.delete(i);
					ctspService.delete(i);
					phienbanspService.delete(i);
				}

			}

			int productID = dto.getId().intValue();
			int soluongnhap = 0;
			int soluongban = 0;

			if (handledVariantIds != null) {
				for (Integer i : handledVariantIds) {
					soluongnhap += pnService.countNumberProductNhap(i);
					soluongban += pxService.countNumberProductSold(i);
				}
			}

			productService.updateSLProduct(soluongnhap, soluongban, productID);
		}

	}

	private void saveVariant(Long productId, String ram, String rom, String color, Integer priceN, Integer priceX,
			float sale) {
		int priceNInt = priceN;
		int priceXInt = priceX;
		int colorId = colorService.getIdByValue(color);
		Integer ramId = (ram != null) ? ramService.getIdByValue(Integer.parseInt(ram)) : null;
		Integer romId = (rom != null) ? romService.getIdByValue(Integer.parseInt(rom)) : null;

		PhienBanSanPhamDTO variant = new PhienBanSanPhamDTO(productId, romId, ramId, colorId, priceNInt, priceXInt,
				sale / 100, (int) (priceXInt - priceXInt * (sale / 100)));

		phienbanspService.save(variant);
	}
}
