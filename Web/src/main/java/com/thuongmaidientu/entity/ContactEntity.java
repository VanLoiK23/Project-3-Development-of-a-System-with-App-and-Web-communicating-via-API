package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
@NoArgsConstructor
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_kh", nullable = false, length = 100)
    private String tenKhachHang;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "status", nullable = false)
    private Integer status;
}
