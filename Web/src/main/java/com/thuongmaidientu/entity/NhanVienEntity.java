package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "nhanvien")
@Getter
@Setter
@NoArgsConstructor
public class NhanVienEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manv")
    private Integer id;

    @Column(name = "hoten", nullable = false, length = 255)
    private String hoTen;

    @Column(name = "matkhau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "gioitinh", nullable = false, length = 15)
    private String gioiTinh;

    @Column(name = "ngaysinh", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngaySinh;

    @Column(name = "sdt", nullable = false, length = 50)
    private String soDienThoai;

    @Column(name = "diachi", nullable = false, length = 255)
    private String diaChi;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "trangthai", nullable = false)
    private Integer trangThai;
    
    @OneToMany(mappedBy = "nguoiTaoPhieu")
    private List<PhieuNhapEntity> listPhieuNhapCreate = new ArrayList<>();
}
