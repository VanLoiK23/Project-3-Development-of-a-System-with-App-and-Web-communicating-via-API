package com.thuongmaidientu.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phieunhap")
@Getter
@Setter
@NoArgsConstructor
public class PhieuNhapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maphieunhap")
    private Integer id;

    @Column(name = "thoigian", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date thoiGian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manhacungcap", nullable = false)
    private NhaCungCapEntity nhaCungCap;  // Khóa ngoại đến bảng nhà cung cấp

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoitaophieunhap", nullable = false)
    private NhanVienEntity nguoiTaoPhieu;  // Khóa ngoại đến bảng tài khoản (người tạo phiếu)

    @Column(name = "tongtien", nullable = false)
    private BigDecimal tongTien;

    @Column(name = "save", nullable = true, length = 50)
    private String save="active";
    
    @OneToMany(mappedBy = "phieunhap", cascade = CascadeType.ALL)
    private List<ChiTietPhieuNhapEntity> phieunhapList = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "pn")
    private List<CTSanPhamEntity> phieunhapCTSP = new ArrayList<>();
}
