package com.thuongmaidientu.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DiscountDTO extends AbstractDTO<DiscountDTO> {

	private String code;

	private Integer discountAmount;

	private Integer numberUsed;

	private Date expirationDate;

	private Integer paymentLimit;

	private String description;
	
	private Integer num_trash;

}
