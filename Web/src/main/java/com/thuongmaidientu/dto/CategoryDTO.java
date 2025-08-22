package com.thuongmaidientu.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CategoryDTO {

	@JsonProperty("mathuonghieu")
	private Long id;

	@JsonProperty("tenthuonghieu")
	private String tenThuongHieu;

	private String image;

	private Integer num_trash;

	private List<CategoryDTO> listResult = new ArrayList<>();
	private Integer page;
	private Integer limit;
	private Integer totalPage;
	private Integer totalItem;

	private String trash;
	private Integer status;

}