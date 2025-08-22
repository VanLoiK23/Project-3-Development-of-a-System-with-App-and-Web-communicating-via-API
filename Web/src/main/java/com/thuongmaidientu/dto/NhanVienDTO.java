package com.thuongmaidientu.dto;

import java.util.Date;

import lombok.Data;

@Data
public class NhanVienDTO {

    private Integer id;

    private String hoTen;

    private String matKhau;

    private String gioiTinh;

    private Date ngaySinh;

    private String soDienThoai;

    private String diaChi;

    private String email;

    private Integer trangThai;
    

}
