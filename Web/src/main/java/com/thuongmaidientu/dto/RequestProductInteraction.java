package com.thuongmaidientu.dto;

import java.util.List;

import lombok.Data;

@Data
public class RequestProductInteraction {

	private Integer id;
	private String name;
	private String img;
	private Integer xuatxu;
	private Integer hedieuhanh;
	private Integer thuonghieu;
	private Integer khuvuckho;
	private Integer giamgia;
	private Integer giare;
	private Integer dungluongpin;
	private String cameratruoc;
	private String camerasau;
	private Integer phienbanHDH;
	private String manhinh;
	private String title;
	private String description;
	private Boolean isTragop;
	private List<ProductVariant> listPB;

}
