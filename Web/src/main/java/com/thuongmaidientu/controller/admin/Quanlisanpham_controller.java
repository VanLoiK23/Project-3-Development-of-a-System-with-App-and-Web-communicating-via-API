package com.thuongmaidientu.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.CTSanPhamDTO;
import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.ChiTietPhieuNhapDTO;
import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.PhieuNhapDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.PromoDTO;
import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.service.CloudinaryService;
import com.thuongmaidientu.service.ICategotyService;
import com.thuongmaidientu.service.IChiTietPNService;
import com.thuongmaidientu.service.IChiTietPXService;
import com.thuongmaidientu.service.IChiTietSPService;
import com.thuongmaidientu.service.IColorService;
import com.thuongmaidientu.service.IHedieuhanhService;
import com.thuongmaidientu.service.IKhuVucKhoService;
import com.thuongmaidientu.service.INhacungcapService;
import com.thuongmaidientu.service.IPhienbanspService;
import com.thuongmaidientu.service.IPhieuNhapService;
import com.thuongmaidientu.service.IProductService;
import com.thuongmaidientu.service.IRamService;
import com.thuongmaidientu.service.IRomService;
import com.thuongmaidientu.service.IXuatxuService;

import jakarta.servlet.http.HttpSession;

@Controller(value = "Quanlisanpham_controller")
public class Quanlisanpham_controller {

	@Autowired
	private IProductService productService;

	@Autowired
	private IPhienbanspService phienbanspService;

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private INhacungcapService nhacungcapService;

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

	@Autowired
	private IKhuVucKhoService khoService;

	@Autowired
	private IChiTietPNService pnService;

	@Autowired
	private IChiTietPXService pxService;

	@Autowired
	private IChiTietSPService ctspService;

	@Autowired
	private IPhieuNhapService importService;

	@RequestMapping(value = "/quan-tri/Quanlisanpham_controller/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") ProductDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit, @ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/sanpham/quanlisp_view");

		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		model.setListResult(productService.findAll(pageable));
		model.setTotalItem(productService.getTotalItem());
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		model.setNum_trash(productService.getTotalItemTrash());

		// for list import
		List<NhaCungCapDTO> dto = new ArrayList<NhaCungCapDTO>();

		dto.addAll(nhacungcapService.selectAll());

		mav.addObject("model", model);
		mav.addObject("page", page);

		mav.addObject("nhacungcapModel", dto);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		return mav;
	}

	@GetMapping("/quan-tri/Quanlisanpham_controller/status")
	public String disableSanPham(@RequestParam("id") int id, @RequestParam("type") String type,
			@RequestParam(value = "page", defaultValue = "1") int page, RedirectAttributes redirectAttributes) {
		try {
			if ("disable".equals(type)) {
				productService.updateStatus(id, 0);
			} else if ("active".equals(type)) {
				productService.updateStatus(id, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Giữ lại số trang hiện tại khi redirect
		return "redirect:/quan-tri/Quanlisanpham_controller/danh-sach?page=" + page;
	}

	@RequestMapping(value = "/quan-tri/bai-viet/chinh-sua", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView mav = new ModelAndView("admin/new/edit");
		return mav;
	}

	@RequestMapping(value = "/quan-tri/bai-viet/them", method = RequestMethod.GET)
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("admin/home");

		return mav;
	}

//	@RequestMapping(value = "/quan-tri/bai-viet/them/{id}")
//  public String details(@PathVariable("id") int i)

	@GetMapping("/quan-tri/Quanlisanpham_controller/add_sp")
	public ModelAndView showAddSanPhamForm() {
		ModelAndView mav = new ModelAndView("admin/sanpham/add_sp");
		ProductDTO dto = new ProductDTO();

		dto.setCategory_List(categoryService.selectAll());
		dto.setHedieuhanh_List(hedieuhanhService.selectAll());
		dto.setColor_List(colorService.selectAll());
		dto.setRam_List(ramService.selectAll());
		dto.setRom_List(romService.selectAll());
		dto.setXuatxu_List(xuatxuService.selectAll());
		dto.setKho_List(khoService.selectAll());

		mav.addObject("model", dto);
		return mav;
	}

	@GetMapping("/quan-tri/Quanlisanpham_controller/update_sp")
	public ModelAndView showUpdateSanPhamForm(@RequestParam("id_sp") Integer idsp) {
		ModelAndView mav = new ModelAndView("admin/sanpham/edit_sp");
		ProductDTO dto = new ProductDTO();
		List<PhienBanSanPhamDTO> listPBSP = null;

		if (idsp != null) {
			dto = productService.findById(idsp);

			Integer categoryId = dto.getThuongHieuId();

			List<CategoryDTO> categoryList = categoryService.selectAll();

			CategoryDTO currentCategory = null;
			for (CategoryDTO category : categoryList) {
				if (category.getId().equals(categoryId)) {
					currentCategory = category;
					break;
				}
			}

			if (currentCategory != null) {
				categoryList.remove(currentCategory);
				categoryList.add(0, currentCategory);
			}

			dto.setCategory_List(categoryList);

			Integer hedieuhanhId = dto.getHeDieuHanhId();

			List<HedieuhanhDTO> hdhList = hedieuhanhService.selectAll();
			HedieuhanhDTO currentHDH = null;

			if (hedieuhanhId != null) {
				for (HedieuhanhDTO h : hdhList) {
					if (h.getId().equals(hedieuhanhId)) {
						currentHDH = h;
						break;
					}
				}
			}

			// Nếu tìm thấy hệ điều hành, đưa nó lên đầu danh sách
			if (currentHDH != null) {
				hdhList.remove(currentHDH);
				hdhList.add(0, currentHDH);
			}

			// Nếu danh sách rỗng hoặc không có hệ điều hành phù hợp, thêm tùy chọn "Không"
			if (hdhList.isEmpty() || currentHDH == null) {
				HedieuhanhDTO noOS = new HedieuhanhDTO();
				noOS.setId(null); // Đặt ID null để không chọn gì mặc định
				noOS.setTenHeDieuHanh("Không");
				hdhList.add(0, noOS);
			}

			dto.setHedieuhanh_List(hdhList);

			Integer XXId = dto.getXuatXuId();

			List<XuatXuDTO> XXList = xuatxuService.selectAll();

			XuatXuDTO currentXX = null;
			for (XuatXuDTO XX : XXList) {
				if (XX.getId().equals(XXId)) {
					currentXX = XX;
					break;
				}
			}

			if (currentXX != null) {
				XXList.remove(currentXX);
				XXList.add(0, currentXX);
			}

			dto.setXuatxu_List(XXList);

			Integer KhoId = dto.getKhuVucKhoId();

			List<KhuVucKhoDTO> KhoList = khoService.selectAll();

			KhuVucKhoDTO currentKho = null;
			for (KhuVucKhoDTO Kho : KhoList) {
				if (Kho.getId().equals(KhoId)) {
					currentKho = Kho;
					break;
				}
			}

			if (currentKho != null) {
				KhoList.remove(currentKho);
				KhoList.add(0, currentKho);
			}

			dto.setKho_List(KhoList);

//			listPBSP = phienbanspService.findAll(idsp);
			listPBSP = dto.getPbspList();

		} else {
			dto.setCategory_List(categoryService.selectAll());
			dto.setHedieuhanh_List(hedieuhanhService.selectAll());
			dto.setXuatxu_List(xuatxuService.selectAll());
			dto.setKho_List(khoService.selectAll());
		}

		dto.setColor_List(colorService.selectAll());
		dto.setRam_List(ramService.selectAll());
		dto.setRom_List(romService.selectAll());

		if (dto.getPromoList() != null) {
			for (PromoDTO value : dto.getPromoList()) {
				if ("tragop".equals(value.getName())) { // So sánh chuỗi đúng cách
					mav.addObject("tragop", 1);
				}
				if ("giareonline".equals(value.getName())) {
					mav.addObject("giareonline", value.getValue());
				}
				if ("giamgia".equals(value.getName())) {
					mav.addObject("giamgia", value.getValue());
				}
			}
		}
		System.out.println((dto.getPromo()));
		System.out.println((dto.getPromoList()));

		if (listPBSP != null) {
			mav.addObject("pbsp", listPBSP);

			System.out.println(listPBSP);
		}

		mav.addObject("model", dto);
		mav.addObject("imgUrl", dto.getHinhAnh());
		return mav;
	}

	@PostMapping("/quan-tri/Quanlisanpham_controller/add_sp")
	public ModelAndView addSanPham(@ModelAttribute("model") ProductDTO dto, RedirectAttributes redirectAttributes,

			@RequestParam(value = "check", required = false) String tragop,

			@RequestParam(value = "sale", required = false, defaultValue = "0") Float sale,

			@RequestParam(value = "bigsale", required = false, defaultValue = "0") Integer bigsale,

			@RequestParam(value = "ram[]", required = false) List<String> ramList,

			@RequestParam(value = "rom[]", required = false) List<String> romList,

			@RequestParam(value = "color[]", required = false) List<String> colorList,

			@RequestParam(value = "price_n[]", required = false) List<Integer> priceNList,

			@RequestParam(value = "price_x[]", required = false) List<Integer> priceXList,

			@RequestParam(value = "img", required = false) MultipartFile img,

			@RequestParam(value = "img-upload1", required = false) MultipartFile img1,

			@RequestParam(value = "img-upload2", required = false) MultipartFile img2,

			@RequestParam(value = "img-upload3", required = false) MultipartFile img3) {

		System.out.println("DTO: " + dto);

		List<MultipartFile> imageFiles = new ArrayList<>();
		List<String> nameImageFiles = new ArrayList<>();

		if (img != null && !img.isEmpty()) {
			imageFiles.add(img);
			nameImageFiles.add(dto.getTenSanPham());
		}
		if (img1 != null && !img1.isEmpty()) {
			imageFiles.add(img1);
			nameImageFiles.add(dto.getTenSanPham() + "1");
		}
		if (img2 != null && !img2.isEmpty()) {
			imageFiles.add(img2);
			nameImageFiles.add(dto.getTenSanPham() + "2");
		}
		if (img3 != null && !img3.isEmpty()) {
			imageFiles.add(img3);
			nameImageFiles.add(dto.getTenSanPham() + "3");
		}

		List<String> imageUrls = cloudinaryService.uploadMultipleFiles(imageFiles, nameImageFiles);

		String imageUrlsString = (imageUrls != null && !imageUrls.isEmpty()) ? String.join(",", imageUrls) : "";

		dto.setHinhAnh(imageUrlsString);

		// Xử lý khuyến mãi
		List<PromoDTO> promoList = new ArrayList<>();
		if (tragop != null)
			promoList.add(new PromoDTO("tragop", "0"));
		if (sale > 0)
			promoList.add(new PromoDTO("giareonline", String.valueOf(sale)));
		if (bigsale > 0)
			promoList.add(new PromoDTO("giamgia", String.valueOf(bigsale)));
		promoList.add(new PromoDTO("moiramat", "0"));

		dto.setPromo(dto.convertPromoListToJson(promoList));
		dto.setAlias(dto.getTenSanPham().toLowerCase().replace(" ", "-"));

		// Lưu sản phẩm
		ProductDTO savedProduct = productService.save(dto);
		Long productId = savedProduct.getId();

		// Xử lý RAM, ROM, COLOR
		if ((ramList == null || ramList.isEmpty()) && (romList == null || romList.isEmpty())) { // Nếu không có RAM &
																								// ROM, chỉ thêm màu sắc
			saveVariant(productId, null, null, colorList.get(0), priceNList.get(0), priceXList.get(0), sale);
		} else {
			for (int i = 0; i < ramList.size(); i++) {
				saveVariant(productId, ramList.get(i), romList.get(i), colorList.get(i), priceNList.get(i),
						priceXList.get(i), sale);
			}
		}

		redirectAttributes.addFlashAttribute("message", "Add Product successfully!");

		return new ModelAndView("redirect:/quan-tri/Quanlisanpham_controller/danh-sach");
	}

	@PostMapping("/quan-tri/Quanlisanpham_controller/update_sp")
	public ModelAndView updateSanPham(@ModelAttribute("model") ProductDTO dto, RedirectAttributes redirectAttributes,

			@RequestParam(value = "check", required = false) String tragop,

			@RequestParam(value = "sale", required = false, defaultValue = "0") Float sale,

			@RequestParam(value = "bigsale", required = false, defaultValue = "0") Integer bigsale,

			@RequestParam(value = "id_pbsp[]", required = false) List<Long> existingVariantIds,

			@RequestParam(value = "ram[]", required = false) List<String> ramList,

			@RequestParam(value = "rom[]", required = false) List<String> romList,

			@RequestParam(value = "color[]", required = false) List<String> colorList,

			@RequestParam(value = "price_n[]", required = false) List<String> parsedPricesN,

			@RequestParam(value = "price_x[]", required = false) List<String> parsedPricesX,

			@RequestParam(value = "state", required = false) Integer status,

			@RequestParam(value = "img", required = false) MultipartFile img,

			@RequestParam(value = "img-upload1", required = false) MultipartFile img1,

			@RequestParam(value = "img-upload2", required = false) MultipartFile img2,

			@RequestParam(value = "img-upload3", required = false) MultipartFile img3) {

		List<Integer> priceNList = new ArrayList<>();
		if (parsedPricesN != null) {
			for (String priceStr : parsedPricesN) {
				if (priceStr != null && !priceStr.isEmpty()) {
					String cleanedPrice = priceStr.replace(".", "").replace(" đ", "").trim();

					priceNList.add(Integer.parseInt(cleanedPrice));
				}
			}
		}

		List<Integer> priceXList = new ArrayList<>();
		if (parsedPricesX != null) {
			for (String priceStr : parsedPricesX) {
				if (priceStr != null && !priceStr.isEmpty()) {
					String cleanedPrice = priceStr.replace(".", "").replace(" đ", "").trim();

					priceXList.add(Integer.parseInt(cleanedPrice));
				}
			}
		}

		System.out.println("DTO: " + dto);

		String oldImageUrls = productService.selectImages(dto.getId() != null ? dto.getId().intValue() : null); // Lấy
																												// URL
																												// cũ
		List<String> imageList = new ArrayList<>(Arrays.asList(oldImageUrls.split(",")));

		if (img != null && !img.isEmpty()) {
			String newImageUrl = cloudinaryService.uploadFile(img, dto.getTenSanPham()); // Upload ảnh mới
			imageList.set(0, newImageUrl);
		}
		if (img1 != null && !img1.isEmpty()) {
			String newImageUrl1 = cloudinaryService.uploadFile(img1, dto.getTenSanPham() + "1");

			if (imageList.size() > 1) {
				imageList.set(1, newImageUrl1); // Cập nhật ảnh thứ 2
			} else {
				imageList.add(newImageUrl1);
			}
		}
		if (img2 != null && !img2.isEmpty()) {
			String newImageUrl = cloudinaryService.uploadFile(img2, dto.getTenSanPham() + "2"); // Upload ảnh mới
			if (imageList.size() > 2) {
				imageList.set(2, newImageUrl); // Cập nhật ảnh thứ 2
			} else {
				imageList.add(newImageUrl);
			}
		}
		if (img3 != null && !img3.isEmpty()) {
			String newImageUrl = cloudinaryService.uploadFile(img3, dto.getTenSanPham() + "3"); // Upload ảnh mới
			if (imageList.size() > 3) {
				imageList.set(3, newImageUrl); // Cập nhật ảnh thứ 2
			} else {
				imageList.add(newImageUrl);
			}
		}

		String imageUrlsString = (imageList != null && !imageList.isEmpty()) ? String.join(",", imageList) : "";

		dto.setHinhAnh(imageUrlsString);

		dto.setStatus(status);

		// Xử lý khuyến mãi
		List<PromoDTO> promoList = new ArrayList<>();
		if (tragop != null)
			promoList.add(new PromoDTO("tragop", "0"));
		if (sale > 0)
			promoList.add(new PromoDTO("giareonline", String.valueOf(sale)));
		if (bigsale > 0)
			promoList.add(new PromoDTO("giamgia", String.valueOf(bigsale)));

		dto.setPromo(dto.convertPromoListToJson(promoList));
		dto.setAlias(dto.getTenSanPham().toLowerCase().replace(" ", "-"));

		// Lưu sản phẩm
		ProductDTO savedProduct = productService.save(dto);

		// Xử lý RAM, ROM, COLOR
		if ((ramList == null || ramList.isEmpty()) && (romList == null || romList.isEmpty())) {

			Integer colorId = colorService.getIdByValue(colorList.get(0));

			int normalPrice = priceNList.get(0);
			int salePrice = priceXList.get(0);
			Float salePercentage = sale / 100;
			Integer finalPrice = (int) (salePrice - salePrice * (sale / 100));

			PhienBanSanPhamDTO variant = new PhienBanSanPhamDTO(dto.getId(), null, null, colorId, normalPrice,
					salePrice, salePercentage, finalPrice);

			Long variantId;
			if (existingVariantIds != null) {
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
				Float salePercentage = sale / 100;
				Integer finalPrice = (int) (salePrice - salePrice * (sale / 100));

				PhienBanSanPhamDTO variant = new PhienBanSanPhamDTO(dto.getId(), romId, ramId, colorId, normalPrice,
						salePrice, salePercentage, finalPrice);

				Long variantId;
				if (existingVariantIds != null && i < existingVariantIds.size()) {
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

			redirectAttributes.addFlashAttribute("message", idsToDelete);
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
			
			//check
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

		redirectAttributes.addFlashAttribute("message", "Update Product successfully!");

		return new ModelAndView("redirect:/quan-tri/Quanlisanpham_controller/danh-sach");
	}

	/*
	 * @RequestMapping(value = "/uploadFile", method = RequestMethod.POST) public
	 * String uploadFile(MyFile myFile, RedirectAttributes redirectAttributes,
	 * HttpServletRequest request) { redirectAttributes.addFlashAttribute("message",
	 * "Upload success"); redirectAttributes.addFlashAttribute("description",
	 * myFile.getDescription());
	 * 
	 * try { MultipartFile multipartFile = myFile.getMultipartFile(); String
	 * fileName = multipartFile.getOriginalFilename();
	 * 
	 * // Chọn cách lưu (chỉ chọn 1) // Cách 1: Lưu trong thư mục của ứng dụng
	 * String uploadDir = request.getServletContext().getRealPath("/uploads");
	 * 
	 * // // Cách 2: Lưu vào thư mục ngoài Tomcat (D:/files) // String uploadDir =
	 * "D:/files"; // Comment dòng này nếu chọn cách 1
	 * 
	 * // Kiểm tra và tạo thư mục nếu chưa có File dir = new File(uploadDir); if
	 * (!dir.exists()) { dir.mkdirs(); }
	 * 
	 * // Lưu file vào thư mục File file = new File(dir, fileName);
	 * multipartFile.transferTo(file);
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * redirectAttributes.addFlashAttribute("message", "Upload failed"); }
	 * 
	 * return "redirect:/result"; }
	 */

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

	@PostMapping("/quan-tri/Quanlisanpham_controller/import")
	public ModelAndView importSP(RedirectAttributes redirectAttributes,

			@RequestParam(value = "mapbsp") List<Integer> id,

			@RequestParam(value = "price", defaultValue = "0") List<Integer> price,

			@RequestParam(value = "number", defaultValue = "0") List<Integer> number,

			@RequestParam(value = "imei", required = false) List<Long> imei,

			@RequestParam(value = "supplier", required = false) Integer supplier,

			HttpSession session) {

		Double total = 0.0;
		for (int i = 0; i < id.size(); i++) {

			int currentPrice = price.get(i);
			int currentNumber = number.get(i);

			total += currentNumber * currentPrice;
		}

//		      $_SESSION['id']

		PhieuNhapDTO saveImportDto = new PhieuNhapDTO();
		UserDTO user = (UserDTO) session.getAttribute("user");
		if (user == null) {
			return new ModelAndView("redirect:/dang-nhap"); // chưa đăng nhập
		} else {
			saveImportDto.setNguoiTaoPhieuId(user.getId().intValue());
		}
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


		redirectAttributes.addFlashAttribute("message", "Import Product successfully!");

		return new ModelAndView("redirect:/quan-tri/Quanlisanpham_controller/danh-sach");
	}

	@GetMapping("/quan-tri/Quanlisanpham_controller/trash")
	public ModelAndView showTrashProduct(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/sanpham/trash_sp");

		List<ProductDTO> dto = new ArrayList<ProductDTO>();
		dto = productService.findTrash();

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		mav.addObject("model", dto);

		return mav;
	}

	@GetMapping("/quan-tri/Quanlisanpham_controller/reset/{id}")
	public ModelAndView reset(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {

		productService.updateTrash(id, "active");

		redirectAttributes.addFlashAttribute("message", "Reset Product successfully!");

		return new ModelAndView("redirect:/quan-tri/Quanlisanpham_controller/trash");
	}

	@GetMapping("/quan-tri/Quanlisanpham_controller/Delete_permanent/{id}")
	public ModelAndView deleteProduct(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {

		productService.delete(id);

		redirectAttributes.addFlashAttribute("message", "Deleted Product successfully!");

		return new ModelAndView("redirect:/quan-tri/Quanlisanpham_controller/trash");
	}

//	@RequestMapping(value = "/quan-tri/bai-viet/them", method = RequestMethod.POST)
//	public ModelAndView add(@ModelAttribute("model") NewDTO newDTO) {
//		productService.save(newDTO); // Lưu bài viết vào DB
//		return new ModelAndView("redirect:/quan-tri/bai-viet/danh-sach"); // Chuyển về danh sách
//	}

	// in loi
//	@ExceptionHandler(Exception.class)
//	@ResponseBody
//	public String handleException(Exception ex) {
//		// In lỗi trực tiếp
//		return "Lỗi: " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace());
//	}
}