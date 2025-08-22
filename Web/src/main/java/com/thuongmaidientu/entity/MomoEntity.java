package com.thuongmaidientu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "momo")
@Getter
@Setter
@NoArgsConstructor
public class MomoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_momo")
    private Integer idMomo;

    @Column(name = "partner_code", nullable = false, length = 50)
    private String partnerCode;

    @Column(name = "order_Id", nullable = false)
    private Long orderId;

    @Column(name = "amount", nullable = false, length = 50)
    private String amount;

    @Column(name = "order_info", nullable = false, length = 100)
    private String orderInfo;

    @Column(name = "order_type", nullable = false, length = 50)
    private String orderType;

    @Column(name = "trans_id", nullable = false)
    private Long transId;

    @Column(name = "pay_Type", nullable = false, length = 50)
    private String payType;

    @Column(name = "code_cart", nullable = false, length = 50)
    private String codeCart;
}
