package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "sort_Desc", length = 255, nullable = false)
    private String sortDesc;

    @Column(name = "image", length = 100, nullable = false)
    private String image;

    @Column(name = "details", columnDefinition = "LONGTEXT", nullable = false)
    private String details;

    @Column(name = "create_at", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date createAt = new Date();

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private Integer status;

    @Column(name = "trash", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'active'")
    private String trash;

    @Column(name = "deleted_at", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deletedAt;
    
    @OneToMany(mappedBy = "article")
    private List<CommentArticleEntity> listComment = new ArrayList<>();
}
