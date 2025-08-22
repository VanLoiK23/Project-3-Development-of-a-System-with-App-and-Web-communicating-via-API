package com.thuongmaidientu.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuongmaidientu.dto.ChiTietPhieuNhapDTO;
import com.thuongmaidientu.dto.PhieuNhapDTO;
import com.thuongmaidientu.service.IChiTietPNService;
import com.thuongmaidientu.service.IPhieuNhapService;
import com.thuongmaidientu.service.IProductService;

import jakarta.servlet.http.HttpServletResponse;

@Controller("PhieuNhapOfController")
public class PhieuNhapController {

	@Autowired
	private IPhieuNhapService phieuNhapService;

	@Autowired
	private IChiTietPNService chiTietPNService;

	@Autowired
	private IProductService productService;

	@RequestMapping(value = "/quan-tri/phieunhap/danh-sach", method = RequestMethod.GET)
	public ModelAndView showlist(@ModelAttribute("model") PhieuNhapDTO model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "7") int limit,
			@RequestParam(value = "timeFilter", required = false, defaultValue = "all") String timeFilter,
			@ModelAttribute("message") String message) {

		ModelAndView mav = new ModelAndView("admin/phieunhap/quanli_phieunhap_view");
		model.setPage(page);
		model.setLimit(limit);

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("id")));

		// Tạo khoảng ngày từ filter
		LocalDate fromDate = null;
		switch (timeFilter) {
		case "today":
			fromDate = LocalDate.now();
			break;
		case "yesterday":
			fromDate = LocalDate.now().minusDays(1);
			break;
		case "week":
			fromDate = LocalDate.now().with(DayOfWeek.MONDAY);
			break;
		case "month":
			fromDate = LocalDate.now().withDayOfMonth(1);
			break;
		case "year":
			fromDate = LocalDate.now().withDayOfYear(1);
			break;
		default:
			break;
		}

		List<PhieuNhapDTO> list;
		int totalItem;

		if (fromDate != null) {
			list = phieuNhapService.findAllByDate(Date.valueOf(fromDate), pageable);
			totalItem = phieuNhapService.getTotalItemByDate(Date.valueOf(fromDate));
		} else {
			list = phieuNhapService.findAll(pageable);
			totalItem = phieuNhapService.getTotalItem();
		}

		model.setListResult(list);
		model.setTotalItem(totalItem);
		model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getLimit()));
		model.setNum_trash(phieuNhapService.getTotalItemTrash());

		mav.addObject("model", model);
		mav.addObject("page", page);

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message);
		}
		mav.addObject("timeFilter", timeFilter);

		return mav;
	}

	@GetMapping("/quan-tri/phieunhap/trash")
	public ModelAndView showTrashPN(@ModelAttribute("message") String message) {
		ModelAndView mav = new ModelAndView("admin/phieunhap/trash_phieunhap");

		PhieuNhapDTO dto = new PhieuNhapDTO();
		dto.setListResult(phieuNhapService.findTrash());

		if (message != null && !message.isEmpty()) {
			mav.addObject("message", message); // Add the message to the model for display
		}

		mav.addObject("model", dto);

		return mav;
	}

	@GetMapping("/quan-tri/phieunhap/reset/{id}")
	public ModelAndView reset(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {

		phieuNhapService.updateTrash(id, "active");

		redirectAttributes.addFlashAttribute("message", "Khôi phục phiếu nhập thành công!");

		return new ModelAndView("redirect:/quan-tri/phieunhap/trash");
	}

	@GetMapping("/quan-tri/phieunhap/export/{id}")
	public void exportPhieuNhap(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
		// Lấy thông tin phiếu nhập từ service
		PhieuNhapDTO dto = phieuNhapService.findById(id.intValue());

		List<ChiTietPhieuNhapDTO> chiTietPhieuNhapDTOs = chiTietPNService.getListCTPN(dto.getId().intValue());

		List<Map<String, Object>> saveObjects = new ArrayList<>();

		for (ChiTietPhieuNhapDTO it : chiTietPhieuNhapDTOs) {
			Object[] productData = productService
					.findChiTietPhienBanSanPhamByPhieuNhap(it.getPhienBanSanPhamNhapId(), dto.getId().intValue())
					.get(0);

			Map<String, Object> productInfo = new HashMap<>();

			List<Long> imeisList = new ArrayList<>();
			for (Object[] result : productService.findChiTietPhienBanSanPhamByPhieuNhap(it.getPhienBanSanPhamNhapId(),
					dto.getId().intValue())) {
				imeisList.add((Long) result[6]); // hoặc: Long.parseLong(result[6].toString())
			}

			productInfo.put("product_info", productData);
			productInfo.put("imeis", imeisList);

			saveObjects.add(productInfo);
		}

		dto.setProduct_info(saveObjects);

		// Cấu hình response để tải file Word
		String fileName = "Phieu_Nhap_San_Pham_" + id + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date())
				+ ".doc";
		response.setContentType("application/msword");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		// Tạo nội dung HTML
		PrintWriter writer = response.getWriter();
		writer.println("<html xmlns:o='urn:schemas-microsoft-com:office:office' "
				+ "xmlns:w='urn:schemas-microsoft-com:office:word' " + "xmlns='http://www.w3.org/TR/REC-html40'>");
		writer.println("<head><meta charset='UTF-8'>");
		writer.println("<style>");
		writer.println("body { font-family: 'Times New Roman'; }");
		writer.println("div, p, td { font-size: 20px; } th { font-size: 24px; font-weight: bold; }");
		writer.println("</style></head><body>");

		// In tiêu đề
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String currentTime = sdf.format(new java.util.Date());

		writer.println("<table style='width: 100%; margin: 0px 50px;'>");
		writer.println("<tr>");
		writer.println("<td><strong>Thế giới di động</strong></td>");
		writer.println("<td style='text-align: right;'><strong>Thời gian in phiếu:</strong> " + currentTime + "</td>");
		writer.println("</tr>");
		writer.println("</table><br><br>");

		
		writer.println("<h2 style='text-align:center;'>THÔNG TIN PHIẾU NHẬP</h2>");

		writer.println("<div style='margin-left:50px;'>");
		writer.println("<p><strong>Mã phiếu:</strong> PN-" + dto.getId() + "</p>");
		writer.println("<p><strong>Nhà cung cấp:</strong> " + dto.getNhaCungCapName() + " -       "+ dto.getAddress() +"</p>");
		writer.println("<p><strong>Người thực hiện:</strong> " + dto.getNguoiTaoPhieuName() + " - <strong>Mã NV:</strong> "
				+ dto.getNguoiTaoPhieuId() + "</p>");
		writer.println("<p><strong>Thời gian nhập:</strong> " + dto.getThoiGian() + "</p>");
		writer.println("</div>");

		// Bảng sản phẩm
		writer.println("<table border='1' style='width:100%; margin: 30px 50px; text-align:center; white-space: nowrap;'>");
		writer.println(
				"<tr><th>Tên sản phẩm</th><th>Phiên bản</th><th>Giá</th><th>Số lượng</th><th>Tổng tiền</th></tr>");

		int index = 1;
		Double totalDouble = 0.0;
		NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
		for (Map<String, Object> item : dto.getProduct_info()) {

		    Object[] product = (Object[]) item.get("product_info");
		    List<Long> imeis = (List<Long>) item.get("imeis");

		    // Lấy các thông tin sản phẩm
		    String tenSanPham = (String) product[1];
		    Integer ram = (Integer) product[2];
		    Integer rom = (Integer) product[3];
		    String mauSac = (String) product[4];
		    Number priceNumber = (Number) product[5];
		    Double giaNhap = priceNumber.doubleValue();


		    String formattedGia = formatter.format(giaNhap) + " ₫";

		    Double total = giaNhap * imeis.size();
		    totalDouble += total;

		    writer.println("<tr>");
		    writer.println("<td>" + tenSanPham + "</td>");
		    writer.println("<td>" + ram + " GB - " + rom + " GB - " + mauSac + "</td>");
		    writer.println("<td>" + formattedGia + "</td>");
		    writer.println("<td>" + imeis.size() + "</td>");
		    writer.println("<td>" + formatter.format(total) + " ₫" + "</td>");
		    writer.println("</tr>");
		}

		writer.println("</table>");
		
		writer.println("<p style='margin-left:50px; font-size:24px;'><strong>Thành tiền: <span style='color:red;font-size: 28px;'>" + formatter.format(totalDouble) + " ₫  </span></strong></p>");

		// Bảng chữ ký
		writer.println("<table style='width:100%; margin: 50px auto 0 auto; text-align:center;'>");
		writer.println("<tr>");
		writer.println("<td><strong>Người lập phiếu</strong><br/><span style='font-size: 12px;'>(Ký và ghi rõ họ tên)</span></td>");
		writer.println("<td><strong>Nhân viên nhận</strong><br/><span style='font-size: 12px;'>(Ký và ghi rõ họ tên)</span></td>");
		writer.println("<td><strong>Nhà cung cấp</strong><br/><span style='font-size: 12px;'>(Ký và ghi rõ họ tên)</span></td>");
		writer.println("</tr>");
		writer.println("</table>");

		writer.println("</body></html>");
	}

}
