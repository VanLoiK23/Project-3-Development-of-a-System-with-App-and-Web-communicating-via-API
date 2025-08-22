package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "taikhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "username", length = 255, nullable = false)
    private String username;

    @Column(name = "phone", length = 10, nullable = false)
    private String phone;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "ngaysinh", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngaySinh;

    @Column(name = "gender", length = 50, nullable = true)
    private String gender;

    @Column(name = "password", length = 255, nullable = true)
    private String password;

    @Column(name = "role", length = 50, nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'khách hàng'")
    private String role="khách hàng";

    @Column(name = "time_create_acc", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date timeCreateAcc;

    @Column(name = "reset_token_hash", length = 64, nullable = true)
    private String resetTokenHash;

    @Column(name = "reset_token_expires_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetTokenExpiresAt;
    
    @Column(name = "fcm_tokens", length = 255, nullable = true)
    private String fcm_tokens;
    
    @Column(name = "firebaseUid", length = 255, nullable = true)
    private String firebaseUid;

    @Column(name = "status", columnDefinition = "ENUM('active','deleted','lock') DEFAULT 'active'")
    private String status;

    @Column(name = "deleted_at",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
}
