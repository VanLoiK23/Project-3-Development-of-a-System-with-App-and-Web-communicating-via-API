package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "hedieuhanh")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HedieuhanhEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mahedieuhanh")
    private Integer maHeDieuHanh;

    @Column(name = "tenhedieuhanh", length = 255, nullable = false)
    private String tenHeDieuHanh;

    @Column(name = "status", columnDefinition = "int(1) default 1")
    private Integer status=1;

    @Column(name = "trash", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'active'")
    private String trash="active";

    @Column(name = "delete_at",nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deleteAt;
    
    @OneToMany(mappedBy = "heDieuHanh")
    private List<ProductEntity> hdhs = new ArrayList<>();
}
