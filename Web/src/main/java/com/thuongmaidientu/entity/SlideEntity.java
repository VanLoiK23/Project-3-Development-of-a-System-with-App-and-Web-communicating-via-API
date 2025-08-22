package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "slide")
@Getter
@Setter
@NoArgsConstructor
public class SlideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "image", nullable = false, length = 255)
    private String image;

    @Column(name = "trash", nullable = false, length = 10)
    private String trash="active";

    @Column(name = "status", nullable = false)
    private Integer status=1;

    @Column(name = "deleted_at",nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deletedAt;
}
