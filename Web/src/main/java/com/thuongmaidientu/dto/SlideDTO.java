package com.thuongmaidientu.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlideDTO {

	@JsonProperty("maSlide")
	private Long id;

	private String image;

	private Integer num_trash;

	private String trash;

	private Integer status;
	
	private List<SlideDTO> listResult = new ArrayList<>();
	private Integer page;
	private Integer limit;
	private Integer totalPage;
	private Integer totalItem;

}
