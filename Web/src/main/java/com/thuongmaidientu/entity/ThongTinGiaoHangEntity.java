package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "thongtingiaohang")
@Getter
@Setter
@NoArgsConstructor
public class ThongTinGiaoHangEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idkh", referencedColumnName = "makh")
    private KhachHangEntity khachHangDat;

    @Column(name = "hovaten", length = 100)
    private String hoVaTen;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "sodienthoai", length = 100)
    private String soDienThoai;

    @Column(name = "street_name", length = 255)
    private String streetName;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country="Việt Nam";

    @Column(name = "note", length = 255,nullable = true)
    private String note="";
}
