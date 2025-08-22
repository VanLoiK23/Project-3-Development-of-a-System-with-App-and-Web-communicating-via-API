package com.thuongmaidientu.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class PhieuNhapDTO extends AbstractDTO<PhieuNhapDTO>{

    private Date thoiGian;

    private BigDecimal tongTien;

    private String save;
    
    private Integer nhaCungCapID; 
    
    private String nhaCungCapName; 
    
    private String address; 
    
    private Integer nguoiTaoPhieuId; 
    
    private String nguoiTaoPhieuName; 
    
    private Integer num_trash;
    
    private List<Map<String, Object>> product_info;
    
    

}
