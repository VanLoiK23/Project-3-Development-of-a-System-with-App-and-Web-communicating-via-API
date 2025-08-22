package com.thuongmaidientu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class PromoDTO {
    private String name;
    private String value;
    
	public PromoDTO(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
    
    
}
