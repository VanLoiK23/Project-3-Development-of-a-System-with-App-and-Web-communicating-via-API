package com.thuongmaidientu.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CommentAndRateDTO extends AbstractDTO<CommentAndRateDTO>{
	

    private String content;

    private Double rate;

    private String img;
   
    private Integer id_user;
    @JsonProperty("user")
    private String hoten;

    
    private Integer id_sp;
    private String tensp;

    private Integer feeback;

    @JsonProperty("feeback_content")
    private String feedbackContent;

    private String nhanvien;

    private Date ngayPH;
    
    private String ngayphanhoi;

    private Integer order_id;
    
    private Date ngayDanhGia;
    
    private String ngay_đanhgia;

}
