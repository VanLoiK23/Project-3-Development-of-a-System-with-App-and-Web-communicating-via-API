package com.thuongmaidientu.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class ProductDTO extends AbstractDTO<ProductDTO> {

	@JsonProperty("tensp")
	private String tenSanPham;
	private String alias;
	@JsonProperty("hinhanh")
	private String hinhAnh;

	private Integer xuatXuId;
	@JsonProperty("xuatxu")
	private String xuatXu;

	@JsonProperty("dungluongpin")
	private Integer dungLuongPin;
	@JsonProperty("manhinh")
	private String manHinh;

	private Integer heDieuHanhId;
	@JsonProperty("hedieuhanh")
	private String heDieuHanh;
	@JsonProperty("phienbanhdh")
	private Double phienBanHDH;
	@JsonProperty("camerasau")
	private String cameraSau;
	@JsonProperty("cameratruoc")
	private String cameraTruoc;
	@JsonProperty("thuonghieu")
	private Integer thuongHieuId;

	private String thuongHieu;

	private Integer status;

	private Integer khuVucKhoId;
	@JsonProperty("khuvuckho")
	private String khuVucKho;
	@JsonProperty("soluongnhap")
	private int soLuongNhap = 0;
	@JsonProperty("soluongban")
	private int soLuongBan = 0;
	private int soLuongCon = 0;

	private int num_trash = 0;

	private String promo;
	@JsonProperty("sortDesc")
	private String sortDesc;
	@JsonProperty("detail")
	private String detail;

	private double diemTrungBinhSao = 0.0;
	@JsonProperty("tongsao")
	private double tongSao = 0.0;
	@JsonProperty("soluongdanhgia")
	private Integer soLuongDanhGia = 0;

	private LocalDateTime deletedAt;

	@Override
	@JsonProperty("masp")
	public Long getId() {
		return super.getId();
	}

	@JsonProperty("created")
	public String getCreated() {
		Date createdTimestamp = super.getCreatedDate();
		if (createdTimestamp == null)
			return null;

		// Format về chuỗi "yyyy-MM-dd"
		return new SimpleDateFormat("yyyy-MM-dd").format(createdTimestamp);
	}

	// Convert List<PromoDTO> thành JSON trước khi lưu
	public String convertPromoListToJson(List<PromoDTO> promoList) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(promoList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "[]"; // Trả về mảng rỗng nếu lỗi
		}
	}

	// Convert JSON từ database thành List<PromoDTO>
	@JsonProperty("promo")
	public List<PromoDTO> getPromoList() {
		if (this.promo == null || this.promo.isEmpty()) {
			return Collections.emptyList();
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(this.promo, new TypeReference<List<PromoDTO>>() {
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public void setPromoList(List<PromoDTO> promoList) {
		this.promo = convertPromoListToJson(promoList);
	}

	// model xai cho add_sp,edit_sp
	private List<CategoryDTO> category_List;
	private List<HedieuhanhDTO> hedieuhanh_List;
	private List<MauSacDTO> color_List;
	private List<RamDTO> ram_List;
	private List<RomDTO> rom_List;
	private List<XuatXuDTO> xuatxu_List;
	private List<KhuVucKhoDTO> kho_List;

	private List<PhienBanSanPhamDTO> pbspList;

	// for show list product
	private Double giaXuat;
	private Double priceSale;

	// for config Android
	private Integer kichThuocRom;
	private Integer kichThuocRam;
	private String color;
	private Integer map;

	// for order in web
	private Integer maPX;
	private Boolean isCommentWithOrder;
}
