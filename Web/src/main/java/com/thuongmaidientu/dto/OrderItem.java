package com.thuongmaidientu.dto;

import lombok.Data;

@Data
public class OrderItem {
	private String productName;
	private int quantity;
	private Integer price;
}
