package com.thuongmaidientu.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "discount")
@Getter
@Setter
@NoArgsConstructor
public class DiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", nullable = false, length = 255)
    private String code;

    @Column(name = "discount", nullable = false)
    private Integer discountAmount;

    @Column(name = "number_used", nullable = false)
    private Integer numberUsed;

    @Column(name = "expiration_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    @Column(name = "payment_limit", nullable = false)
    private Integer paymentLimit;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "created", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date created;

    @Column(name = "trash", nullable = false, length = 10)
    private String trash="active";

    @Column(name = "delete_at",nullable = true)
    @Temporal(TemporalType.DATE)
    private Date deleteAt;

    @Column(name = "status", nullable = false)
    private Integer status=1;
}
