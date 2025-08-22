package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idkh")
    private KhachHangEntity khachHang;

    @Column(name = "created_at", nullable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    @Column(name = "status", columnDefinition = "ENUM('active','completed','abandoned') DEFAULT 'active'")
    private String status="active";
    
    @OneToMany(mappedBy = "idCart")
    private List<CartItemEntity> cart = new ArrayList<>();
}
