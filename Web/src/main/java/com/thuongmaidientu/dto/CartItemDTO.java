package com.thuongmaidientu.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CartItemDTO {

	private Integer cart_item_id;

	private Integer cart_id;

	private Integer maphienbansp;

	private Integer soluong;

	private Integer masp;

	private List<CartItemDTO> listResult = new ArrayList<>();

	// for display product in Cart
	private String tensp;
	private String img;
	private Integer priceSale;

}
