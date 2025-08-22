package com.thuongmaidientu.dto;

import java.util.List;

import lombok.Data;

@Data
public class KhuVucKhoDTO extends AbstractDTO<KhuVucKhoDTO>{

    private String tenKhuVuc;

    private String ghiChu;
    
    private List<ProductDTO> products;

    private Integer num_trash;

    
}
