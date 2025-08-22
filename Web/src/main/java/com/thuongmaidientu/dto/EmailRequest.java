package com.thuongmaidientu.dto;

import java.util.List;

import lombok.Data;

@Data
public class EmailRequest {
	private String toEmail;
    private List<OrderItem> orderItems;
    private Integer total;
}
