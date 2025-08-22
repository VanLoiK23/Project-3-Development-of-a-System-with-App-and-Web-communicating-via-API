package com.thuongmaidientu.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dungluongrom")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "madungluongrom")
    private Integer maDungLuongRom;

    @Column(name = "kichthuocrom")
    private Integer kichThuocRom;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private Integer status=1;

    @Column(name = "trash", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'active'")
    private String trash="active";

    @Column(name = "delete_at",nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deletedAt;
    

    @OneToMany(mappedBy = "rom")
    private List<PhienBanSanPhamEntity> roms = new ArrayList<>();
}
