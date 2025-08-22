package com.thuongmaidientu.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CommentArticleDTO extends AbstractDTO<CommentArticleDTO>{


    private String content;

    private Double rating;
    
    private Integer idClient;
    
    private String nameClient;

    private Integer idArticle;
    
    private String nameArticle;


    private Integer feedback;

    private String contentFeedback;

    private String nhanVien;

    private Date ngayDanhGia;
    private String ngayDanhGiaString;
    private String ngayPhanHoiString;
    private Date ngayPhanHoi;
}
