package com.thuongmaidientu.dto;

import lombok.Data;

@Data
public class ContactDTO extends AbstractDTO<ContactDTO>{

    private String tenKhachHang;

    private String content;

    private String title;

    private String email;
}
