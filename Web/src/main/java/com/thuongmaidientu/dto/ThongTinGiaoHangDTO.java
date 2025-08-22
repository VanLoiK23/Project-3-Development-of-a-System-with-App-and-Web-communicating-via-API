package com.thuongmaidientu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ThongTinGiaoHangDTO extends AbstractDTO<ThongTinGiaoHangDTO> {

	private Integer idkh;

	@JsonProperty("hovaten")
	private String hoVaTen;

	private String email;

	@JsonProperty("sodienthoai")
	private String soDienThoai;

	@JsonProperty("street_name")
	private String streetName;

	private String district;

	private String city;

	private String country;

	private String note;

	private String firstName;
	private String lastName;
}
