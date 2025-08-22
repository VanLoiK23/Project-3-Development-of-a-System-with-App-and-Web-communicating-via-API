package com.thuongmaidientu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PhienBanSanPhamDTO {
	
	// masp = 1,
//           maphienbansp = 101,
//           rom = 256,
//           ram = 12,
//           mausac = 1, // 1: Đen
//           gianhap = 25000000.0,
//           giaxuat = 29990000.0,
//           soluongton = 50,
//           sale = 0.0, // Giảm 10%
//           price_sale = 29990000.0
	
	@JsonProperty("maphienbansp")
	private Long maPhienbansanpham;
	
	@JsonProperty("masp")
	private Long maSanpham;

	private Integer romId;
	private String rom;

	private Integer ramId;
	private String ram;

	private int colorId;
	@JsonProperty("mausac")
	private String color;

	@JsonProperty("gianhap")
	private Integer giaNhap;
	@JsonProperty("giaxuat")
	private Integer giaXuat;
	@JsonProperty("soluongton")
	private Integer soLuongTon;
	@JsonProperty("sale")
	private Float sale;
	@JsonProperty("price_sale")
	private Integer priceSale;

	public PhienBanSanPhamDTO(long maSanpham, Integer romId, Integer ramId, int colorId, Integer giaNhap,
			Integer giaXuat, Float sale, Integer priceSale) {
		super();
		this.maSanpham = maSanpham;
		this.romId = romId;
		this.ramId = ramId;
		this.colorId = colorId;
		this.giaNhap = giaNhap;
		this.giaXuat = giaXuat;
		this.sale = sale;
		this.priceSale = priceSale;
	}

	public PhienBanSanPhamDTO() {
		// TODO Auto-generated constructor stub
	}

}
