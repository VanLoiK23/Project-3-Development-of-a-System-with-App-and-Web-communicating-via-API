package com.thuongmaidientu.dto;

import java.util.List;

import lombok.Data;

@Data
public class ImportProductRequest {
	private List<Integer> ids;
	private List<Integer> prices;
	private List<Integer> quantity;
	private List<Long> imei;
	private Long supplier;

}
