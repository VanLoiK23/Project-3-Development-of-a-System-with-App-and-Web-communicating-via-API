package com.thuongmaidientu.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PhieuXuatDTO extends AbstractDTO<PhieuXuatDTO> {

	private Date thoiGian;

	private BigDecimal tongTien;

	private Integer IDKhachHang;

	private String khachHangName;
	
	private String diaChi;

	private String sdt;

	private String codeCart;

	private String payment;

	private Integer cartShipping;

	private Integer discountCode;
	private Integer amountDiscount;
	private String discountCodeRaw;

	private Integer makh;

	private String save;

	private Integer feeTransport;

	private String feeback;

	private Integer num_trash;
	
	private Date date;

	private List<Map<String, Object>> product_info;
	
	private List<ProductDTO> listProductDTOsForDisplay;
	private List<ProductDTO> listProductDTOsForComment;
	
	//info for order side android
	private DiscountDTO InfoOrderDiscount;
	private List<ChiTietPhieuXuatDTO> listctpx;
	private String name;
	private String phone;
	private String address;
	
//	orderData.forEach((item, index) => {
//		item.product_info.forEach((product) => {
//			document.getElementById("voucherCode").value =
//				"PX" + item.maphieuxuat;
//			document.getElementById("employee").value = item.hovaten;
//			document.getElementById("createTime").value = item.thoigian;
//			if (!uniqueMaphienbansp.has(product.maphienbansp)) {
//				uniqueMaphienbansp.add(product.maphienbansp);
//
//				// Cập nhật thông tin đơn hàng
//				var ram = product.kichthuocram
//					? product.kichthuocram + " GB - "
//					: "";
//				var rom = product.kichthuocrom
//					? product.kichthuocrom + " GB - "
//					: "";
//
//				const row = document.createElement("tr");
//				row.innerHTML = `
//      <td>${index + 1}</td>
//      <td>${product.maphienbansp}</td>
//      <td>${product.tensp}</td>
//      <td>${ram}${rom}${product.tenmausac}</td>
//      <td>${formatCurrency(product.price_sale)}</td>
//      <td>${item.soluong}</td>
//    `;
//				tableBody.appendChild(row);
//
//				// Thêm sự kiện `click` cho hàng `row` trong `tableBody`
//				row.addEventListener("click", () => {
//					// Xóa tất cả các hàng hiện có trong `tableBody1`
//					tableBody1.innerHTML = "";
//
//					// Duyệt qua `product_info` để tìm các sản phẩm có `maphienbansp` tương ứng
//					let maimeiIndex = 1; // Khởi tạo STT cho `maimei`
//					item.product_info.forEach((prod) => {
//						if (prod.maphienbansp === product.maphienbansp) {
//							const row1 = document.createElement("tr");
//							row1.innerHTML = `
//            <td>${maimeiIndex}</td>  <!-- Số thứ tự theo đúng thứ tự hiển thị -->
//            <td>${prod.maimei}</td>
//          `;
//							tableBody1.appendChild(row1);
//							maimeiIndex++; // Tăng STT cho mỗi sản phẩm
//						}
//					});
//				});
//			}
//		});
//	});

}
