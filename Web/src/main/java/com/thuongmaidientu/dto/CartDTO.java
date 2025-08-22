package com.thuongmaidientu.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CartDTO{
	private Integer id;

    private Integer idkh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
	@JsonProperty("created_at")
    private Date createdAt ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
	@JsonProperty("updated_at")
    private Date updatedAt ;

    private String status;
    
    private List<CartItemDTO> cartItems;
  
}
