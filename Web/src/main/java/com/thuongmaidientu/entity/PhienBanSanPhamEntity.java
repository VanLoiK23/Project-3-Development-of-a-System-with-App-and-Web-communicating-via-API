package com.thuongmaidientu.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phienbansanpham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhienBanSanPhamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maphienbansp")
    private Integer maPhienBanSP;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masp")
    private ProductEntity sanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rom")
    private RomEntity rom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ram")
    private RamEntity ram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mausac")
    private MauSacEntity mauSac;

    @Column(name = "gianhap")
    private Integer giaNhap;

    @Column(name = "giaxuat")
    private Integer giaXuat;

    @Column(name = "soluongton", columnDefinition = "int default 0")
    private Integer soLuongTon=0;

    @Column(name = "sale")
    private Float sale;

    @Column(name = "price_sale", nullable = false)
    private Integer priceSale;
    
    @OneToMany(mappedBy = "phienbansanpham")
    private List<CartItemEntity> phienbansanphamCartitem = new ArrayList<>();
    
    @OneToMany(mappedBy = "phienBanSanPhamNhap")
    private List<ChiTietPhieuNhapEntity> phienbansanphamChiTietPhieuNhap = new ArrayList<>();
    
    @OneToMany(mappedBy = "phienBanSanPhamXuat")
    private List<ChiTietPhieuXuatEntity> phienbansanphamChiTietPhieuXuat = new ArrayList<>();
    
    @OneToMany(mappedBy = "productVersion")
    private List<CTSanPhamEntity> phienbanspListImei = new ArrayList<>();
}
