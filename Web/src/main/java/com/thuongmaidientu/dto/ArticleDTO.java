package com.thuongmaidientu.dto;

import java.util.List;

import lombok.Data;

@Data
public class ArticleDTO extends AbstractDTO<ArticleDTO> {
    private String title;

    private String sortDesc;

    private String image;

    private String details;    

    private List<CommentArticleDTO> listComment;
    
    private Integer num_trash;

}
