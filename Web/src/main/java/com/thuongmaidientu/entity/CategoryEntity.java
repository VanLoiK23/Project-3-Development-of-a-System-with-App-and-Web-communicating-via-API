package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "thuonghieu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mathuonghieu")
    private Integer maThuongHieu;

    @Column(name = "tenthuonghieu", length = 255)
    private String tenThuongHieu;

    @Column(name = "image", length = 255, nullable = false)
    private String image;

    @Column(name = "status", columnDefinition = "int(1) default 1")
    private Integer status=1;

    @Column(name = "trash", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'active'")
    private String trash="active";

    @Column(name = "delete_at",nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deleteAt;
    

    @OneToMany(mappedBy = "thuongHieu")
    private List<ProductEntity> brands = new ArrayList<>();
}
