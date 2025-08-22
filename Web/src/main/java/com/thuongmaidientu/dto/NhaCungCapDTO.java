package com.thuongmaidientu.dto;

import lombok.Data;

@Data
public class NhaCungCapDTO extends AbstractDTO<NhaCungCapDTO> {

	private String tenNhaCungCap;

	private String diaChi;

	private String email;

	private String soDienThoai;
	
    private Integer num_trash;


}
