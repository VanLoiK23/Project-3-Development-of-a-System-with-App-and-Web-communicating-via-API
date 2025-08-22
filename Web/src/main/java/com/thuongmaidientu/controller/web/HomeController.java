package com.thuongmaidientu.controller.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.SlideDTO;
import com.thuongmaidientu.service.FirebaseService;
import com.thuongmaidientu.service.ICartService;
import com.thuongmaidientu.service.ICategotyService;
import com.thuongmaidientu.service.IChiTietSPService;
import com.thuongmaidientu.service.IPhienbanspService;
import com.thuongmaidientu.service.IPhieuXuatService;
import com.thuongmaidientu.service.IProductService;
import com.thuongmaidientu.service.ISlideService;

import jakarta.servlet.http.HttpServletRequest;

@Controller(value = "HomeControllerOfWeb")
public class HomeController {
	@Autowired
	private IProductService productService;

	@Autowired
	private IPhienbanspService phienbansanphamService;

	@Autowired
	private ISlideService slideService;

	@Autowired
	private ICategotyService categotyService;

	@Autowired
	private IChiTietSPService chiTietSPService;

//	@Autowired
//	private ICartService cartService;
//	
//	@Autowired
//	private FirebaseService firebaseService;

	// cap nhat lai tat ca so luong cho dong bo **** ok !!

	@RequestMapping(value = "/trang-chu", method = RequestMethod.GET)
	public ModelAndView homePage() {
		
//		cartService.getAllCarts().forEach(cart -> {
//			try {
//				firebaseService.insertCartSynchFromMySql(cart);
//				
//				cart.getCartItems().forEach(cartItem->{
//					firebaseService.insertCartItem(cart.getId(), cartItem);
//				});
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		});
		

		ModelAndView mav = new ModelAndView("web/homepage/Homepage");

		// slide
		SlideDTO slideDTO = new SlideDTO();
		slideDTO.setListResult(slideService.findAll(Pageable.unpaged()));

		mav.addObject("slides", slideDTO);

		// brand
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setListResult(categotyService.findAll(Pageable.unpaged()));

		mav.addObject("brands", categoryDTO);

		// Sản phẩm trả góp
		List<ProductDTO> allInstallmentProducts = productService.getProductDTOByHighlight("tragop", true);

		ProductDTO installmentDto = new ProductDTO();
		installmentDto.setListResult(
				allInstallmentProducts.size() > 5 ? allInstallmentProducts.subList(0, 5) : allInstallmentProducts);

		mav.addObject("installmentProduct", installmentDto);
		mav.addObject("installmentProductCount", allInstallmentProducts.size());

		// Sản phẩm giá rẻ online
		List<ProductDTO> allCheapmentProduct = productService.getProductDTOByHighlight("giareonline", true);

		ProductDTO cheapOnlineDto = new ProductDTO();
		cheapOnlineDto.setListResult(
				allCheapmentProduct.size() > 5 ? allCheapmentProduct.subList(0, 5) : allCheapmentProduct);
		mav.addObject("cheapmentProduct", cheapOnlineDto);
		mav.addObject("cheapmentProductCount", allCheapmentProduct.size());

		// Sản phẩm giảm giá
		List<ProductDTO> allDiscountProduct = productService.getProductDTOByHighlight("giamgia", true);

		ProductDTO discountDto = new ProductDTO();
		discountDto
				.setListResult(allDiscountProduct.size() > 5 ? allDiscountProduct.subList(0, 5) : allDiscountProduct);
		mav.addObject("discountProduct", discountDto);
		mav.addObject("discountProductCount", allDiscountProduct.size());

		// Sản phẩm được đánh giá cao
		List<ProductDTO> allTopRateProduct = productService.getProductDTO_TopRated(3, true);

		ProductDTO topRatedDto = new ProductDTO();
		topRatedDto.setListResult(allTopRateProduct.size() > 5 ? allTopRateProduct.subList(0, 5) : allTopRateProduct);
		mav.addObject("topRateProduct", topRatedDto);
		mav.addObject("topRateProductCount", allTopRateProduct.size());

		// Sản phẩm được mới ra mắt
		List<ProductDTO> allNewReleaseProduct = productService.getProductDTO_NewRelease(true);

		ProductDTO newReleaseProduct = new ProductDTO();
		newReleaseProduct.setListResult(
				allNewReleaseProduct.size() > 5 ? allNewReleaseProduct.subList(0, 5) : allNewReleaseProduct);
		mav.addObject("newReleaseProduct", newReleaseProduct);
		mav.addObject("newReleaseProductCount", allNewReleaseProduct.size());

		// Sản phẩm giá dưới 2 triệu
//	    List<ProductDTO> cheapestProductList = new ArrayList<>();
//	    for (ProductDTO product : productService.getALlProducts()) {
//	        List<PhienBanSanPhamDTO> versions = phienbansanphamService.findAll(product.getId().intValue());
//	        for (PhienBanSanPhamDTO version : versions) {
//	            if (version.getPriceSale() < 2_000_000) {
//	                cheapestProductList.add(product);
//	                break; 
//	            }
//	        }
//	    }
		List<ProductDTO> allCheapestProduct = productService.getProductDTOUnder2M(true);

		ProductDTO cheapUnder2MDto = new ProductDTO();
		cheapUnder2MDto
				.setListResult(allCheapestProduct.size() > 5 ? allCheapestProduct.subList(0, 5) : allCheapestProduct);
		mav.addObject("cheapestProduct", cheapUnder2MDto);
		mav.addObject("cheapestProductCount", allCheapestProduct.size());

		System.out.println("Testing " + topRatedDto.getListResult());

		return mav;
	}

	@GetMapping(value = "/filter")
	public ModelAndView filter(HttpServletRequest request,
			@RequestParam(value = "price", required = false) String rangePrice,
			@RequestParam(value = "promo", required = false) String promo,
			@RequestParam(value = "star", required = false) Integer star,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "company", required = false) String company,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

		List<String> filters = Arrays.asList("promo", "star", "price", "sort", "company", "search");

		// Nếu không có tham số lọc, redirect về trang-chu
		if (rangePrice == null && promo == null && star == null && sort == null && company == null && search == null) {
			return new ModelAndView("redirect:/trang-chu");
		}

		Integer companyId = null;
		if (company != null) {
			companyId = categotyService.getIdCategory(company);
		}

		ModelAndView mav = new ModelAndView("web/homepage/Homepage");

		// slide
		SlideDTO slideDTO = new SlideDTO();
		slideDTO.setListResult(slideService.findAll(Pageable.unpaged()));
		mav.addObject("slides", slideDTO);

		// brand
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setListResult(categotyService.findAll(Pageable.unpaged()));
		mav.addObject("brands", categoryDTO);

		// Xử lý lọc giá nếu có
		Integer startPriceInteger = null;
		Integer endPriceInteger = null;
		if (rangePrice != null) {
			String[] parts = rangePrice.split("-");
			startPriceInteger = Integer.parseInt(parts[0]);
			endPriceInteger = Integer.parseInt(parts[1]);
		}

		ProductDTO model = new ProductDTO();
		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("masp")));

		Map<String, String> criteriaMap = new LinkedHashMap<>();
		criteriaMap.put("price", "min_price");
		criteriaMap.put("star", "average_rating");
		criteriaMap.put("rateCount", "soluongdanhgia");
		criteriaMap.put("name", "tensp");

		if (sort != null) {
			String[] parts = sort.split("-");
			if (parts.length == 2) {
				String criteria = parts[0];
				String order = parts[1];

				String orderCriteria = criteriaMap.get(criteria);

				// Chỉ tạo pageable nếu orderCriteria không null
				if (orderCriteria != null) {
					if ("ascending".equals(order)) {
						pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc(orderCriteria)));
					} else if ("decrease".equals(order) || "descending".equals(order)) {
						pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.desc(orderCriteria)));
					}
				}
			}
		}

		List<ProductDTO> productDTOs = productService.filterProducts(promo, star,
				startPriceInteger != null ? startPriceInteger.doubleValue() : null,
				endPriceInteger != null ? endPriceInteger.doubleValue() : null, companyId, search, pageable);

//		List<ProductDTO> distinctList = new ArrayList<>(new HashSet<>(productDTOs));

		model.setListResult(productDTOs);
		model.setTotalItem(productService.countFilterProducts(promo, star,
				startPriceInteger != null ? startPriceInteger.doubleValue() : null,
				endPriceInteger != null ? endPriceInteger.doubleValue() : null, companyId, search));
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));

		System.out.println(productDTOs.size());
		Integer checkIsOneFilter = 0;
		for (String filter : filters) {
			String filterItem = filter.substring(0, 1).toUpperCase() + filter.substring(1).toLowerCase();
			String urlWithoutFilterItem = buildFilterUrl(request, filter);

			if (request.getParameter(filter) != null) {
				checkIsOneFilter++;
			}

			System.out.println(urlWithoutFilterItem);

			mav.addObject("urlWithout" + filterItem, urlWithoutFilterItem);
		}

		// override
		if (checkIsOneFilter == 1) {
			mav.addObject("checkIsOneFilter", "http://localhost:8080/Spring-mvc/trang-chu");
		}

		mav.addObject("model", model);

		Map<String, String> priceMap = new LinkedHashMap<>();
		priceMap.put("0-2000000", "Dưới 2 triệu");
		priceMap.put("2000000-4000000", "Từ 2 - 4 triệu");
		priceMap.put("4000000-7000000", "Từ 4 - 7 triệu");
		priceMap.put("7000000-13000000", "Từ 7 - 13 triệu");
		priceMap.put("13000000-100000000", "Trên 13 triệu");
		mav.addObject("priceMap", priceMap);

		Map<String, String> sortMap = new LinkedHashMap<>();
		sortMap.put("price-ascending", "Giá tăng dần");
		sortMap.put("price-decrease", "Giá giảm dần");
		sortMap.put("star-ascending", "Sao tăng dần");
		sortMap.put("star-decrease", "Sao giảm dần");
		sortMap.put("rateCount-ascending", "Đánh giá tăng dần");
		sortMap.put("rateCount-decrease", "Đánh giá giảm dần");
		sortMap.put("name-ascending", "Tên A-Z");
		sortMap.put("name-decrease", "Tên Z-A");
		mav.addObject("sortMap", sortMap);

		Map<String, String> promoMap = new LinkedHashMap<>();
		promoMap.put("giamgia", "Giảm giá");
		promoMap.put("giareonline", "Giá rẻ online");
		promoMap.put("tragop", "Trả góp");
		promoMap.put("moiramat", "Mới ra mắt");
		mav.addObject("promoMap", promoMap);

		Map<String, String> starMap = new LinkedHashMap<>();
		starMap.put("2", "Trên 2 sao");
		starMap.put("3", "Trên 3 sao");
		starMap.put("4", "Trên 4 sao");
		mav.addObject("starMap", starMap);

		return mav;
	}

	public String buildFilterUrl(HttpServletRequest request, String excludeParam) {
		StringBuilder urlBuilder = new StringBuilder("?");
		Map<String, String[]> parameterMap = request.getParameterMap();

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();

			if ((!key.equals(excludeParam)) && (!key.equals("page")) && (!key.equals("limit"))) { // Nếu KHÔNG phải là
																									// param cần loại bỏ
																									// va page, limit
				for (String value : entry.getValue()) {
					urlBuilder.append(key).append("=").append(value).append("&");
				}
			}
		}

		// Xóa dấu & cuối nếu có
		if (urlBuilder.length() > 1 && urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
			urlBuilder.deleteCharAt(urlBuilder.length() - 1);
		}

		// Trường hợp chỉ còn "?" (tức là không có gì)
		if (urlBuilder.toString().equals("?")) {
			return "";
		}

		return urlBuilder.toString();
	}

	@RequestMapping("/error/404")
	public String error404() {
		return "web/404"; // Tên trang JSP hoặc Thymeleaf
	}

	@RequestMapping("/setup")
	public void setUpAllQuantity() {
		productService.findAll(Pageable.unpaged()).forEach(sp -> {

			Integer masp = sp.getId().intValue();

			phienbansanphamService.selectAllMaPBSPByMaSP(sp.getId().intValue()).forEach(maPBSP -> {
				Integer quantity = chiTietSPService.getQuantityVersionProduct(masp, maPBSP);

				phienbansanphamService.setUpQuantity(maPBSP, quantity);
			});

			Integer quantityInStock = chiTietSPService.getQuantityProductByStatus(masp, 0);
			Integer quantityExport = chiTietSPService.getQuantityProductByStatus(masp, 1);

			Integer quantityImport = quantityExport + quantityInStock;
			productService.updateSLProduct(quantityImport, quantityExport, masp);

		});
	}

	@RequestMapping("/error/403")
	public String error403() {
		return "web/403"; // Tên trang JSP hoặc Thymeleaf
	}

}
