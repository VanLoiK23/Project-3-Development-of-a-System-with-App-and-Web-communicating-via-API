package com.thuongmaidientu.controller.web;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.service.ICommentAndRateService;
import com.thuongmaidientu.service.IPhienbanspService;
import com.thuongmaidientu.service.IProductService;

@Controller(value = "ProductControllerOfWeb")
public class ProductController {
	@Autowired
	private IProductService productService;

	@Autowired
	private IPhienbanspService phienbansanphamService;

	@Autowired
	private ICommentAndRateService commentAndRateService;

	@RequestMapping(value = "/Detail", method = RequestMethod.GET)
	public ModelAndView Detail(@RequestParam("p") String p) {
		ModelAndView mav = new ModelAndView("web/homepage/details_products");

		String alias = null;
		String config = null;
		Integer ram = null;
		Integer rom = null;
		String color = null;

		String promoFirst = "";

		Pattern pattern = Pattern.compile("(\\d+|null)gb", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(p);

		if (matcher.find()) {
			int index = matcher.start();

			// Tách chuỗi tại vị trí đó
			alias = p.substring(0, index - 1); // bỏ dấu '-'
			config = p.substring(index);

			String[] parts = config.split("-");

			if (parts != null && parts.length >= 3) {
				String ramStr = parts[0].replaceAll("[^0-9]", "");
				String romStr = parts[1].replaceAll("[^0-9]", "");

				ram = ramStr.isEmpty() ? null : Integer.parseInt(ramStr);
				rom = romStr.isEmpty() ? null : Integer.parseInt(romStr);
				color = (parts[2]);
			}
		} else {
			alias = p;
		}

		ProductDTO model = new ProductDTO();

		CommentAndRateDTO commentAndRateDTO = new CommentAndRateDTO();

		ProductDTO sampleProduct = new ProductDTO();

		List<ProductDTO> listResult = productService.findByAlias(alias);

		if (listResult != null) {
			model = listResult.get(0);

			promoFirst = (model.getPromoList() != null && !model.getPromoList().isEmpty())
					? model.getPromoList().get(0).getName()
					: "";
			sampleProduct.setListResult(productService.getProductDTOByHighlight(promoFirst, true));

			mav.addObject("sampleProduct", sampleProduct);

			System.out.println(" ram = " + ram + ", rom = " + rom + ", color = " + color);

			if (config != null) {
//				PhienBanSanPhamDTO phienBanSanPhamDTO = phienbansanphamService.findByConfig(model.getId().intValue(),
//						ram, rom, color);

				for (PhienBanSanPhamDTO item : model.getPbspList()) {
					boolean matchExact = ram != null && rom != null && color != null
							&& item.getRam().equals(ram.toString()) && item.getRom().equals(rom.toString())
							&& item.getColor().trim().equalsIgnoreCase(color.trim());

					boolean matchRomOnly = ram != null && rom != null && color != null
							&& item.getRom().equals(rom.toString()) && (!item.getRam().equals(ram.toString())
									|| !item.getColor().trim().equalsIgnoreCase(color.trim()));

					if (matchExact) {
						model.setColor(item.getColor());
						model.setKichThuocRam(Integer.parseInt(item.getRam()));
						model.setKichThuocRom(Integer.parseInt(item.getRom()));
						model.setGiaXuat(item.getGiaXuat().doubleValue());
						model.setPriceSale(item.getPriceSale().doubleValue());
						break; // đã khớp chính xác, dừng vòng lặp luôn
					} else if (matchRomOnly) {
						model.setColor(item.getColor());
						model.setKichThuocRam(Integer.parseInt(item.getRam()));
						model.setKichThuocRom(Integer.parseInt(item.getRom()));
						model.setGiaXuat(item.getGiaXuat().doubleValue());
						model.setPriceSale(item.getPriceSale().doubleValue());
						// không break ở đây, vì nếu tìm được bản khớp chính xác phía sau vẫn muốn
						// override
					} else if (ram == null) {
						model.setColor(item.getColor());
//						model.setKichThuocRam(Integer.parseInt(item.getRam()));
//						model.setKichThuocRom(Integer.parseInt(item.getRom()));
						model.setGiaXuat(item.getGiaXuat().doubleValue());
						model.setPriceSale(item.getPriceSale().doubleValue());
						break;
					}
				}

//				if (phienBanSanPhamDTO != null) {
//					model.setColor(color);
//					model.setKichThuocRam(ram);
//					model.setKichThuocRom(rom);
//
//					model.setGiaXuat(phienBanSanPhamDTO.getGiaXuat().doubleValue());
//					model.setPriceSale(phienBanSanPhamDTO.getPriceSale().doubleValue());
//				}
			} else {

				if (model.getPbspList() != null && !model.getPbspList().isEmpty()) {
					model.setColor(model.getPbspList().get(0).getColor());

					ram = (model.getPbspList().get(0).getRam() != null)
							? Integer.parseInt(model.getPbspList().get(0).getRam())
							: null;
					rom = (model.getPbspList().get(0).getRom() != null)
							? Integer.parseInt(model.getPbspList().get(0).getRom())
							: null;

					model.setKichThuocRam(ram);
					model.setKichThuocRom(rom);

					model.setGiaXuat(model.getPbspList().get(0).getGiaXuat().doubleValue());
					model.setPriceSale(model.getPbspList().get(0).getPriceSale().doubleValue());
				}
			}

			List<CommentAndRateDTO> commentAndRateDTOs = commentAndRateService
					.findByProductId(model.getId().intValue());
			
			List<CommentAndRateDTO> sortedComments = commentAndRateDTOs.stream()
				    .sorted(Comparator.comparing(CommentAndRateDTO::getNgayDanhGia).reversed()) // mới nhất lên đầu
				    .collect(Collectors.toList());
			
			commentAndRateDTO.setListResult(sortedComments);
		}

		mav.addObject("model", model);
		mav.addObject("comments", commentAndRateDTO);

		return mav;
	}

}
