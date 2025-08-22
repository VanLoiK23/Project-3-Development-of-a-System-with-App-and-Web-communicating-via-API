package com.thuongmaidientu.dto;

import lombok.Data;

@Data
public class XuatXuDTO extends AbstractDTO<XuatXuDTO> {
    private String tenXuatXu;
    
    private Integer num_trash;
}
