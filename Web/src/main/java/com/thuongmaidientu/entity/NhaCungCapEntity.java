package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "nhacungcap")
@Getter
@Setter
@NoArgsConstructor
public class NhaCungCapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manhacungcap")
    private Integer id;

    @Column(name = "tennhacungcap", nullable = false, length = 255)
    private String tenNhaCungCap;

    @Column(name = "diachi", nullable = false, length = 255)
    private String diaChi;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "sdt", nullable = false, length = 255)
    private String soDienThoai;

    @Column(name = "status", nullable = false)
    private Integer status=1;

    @Column(name = "trash", nullable = false, length = 10)
    private String trash="active";

    @Column(name = "delete_at",nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deleteAt;
    
    @OneToMany(mappedBy = "nhaCungCap")
    private List<PhieuNhapEntity> listProductProvide = new ArrayList<>();
}
