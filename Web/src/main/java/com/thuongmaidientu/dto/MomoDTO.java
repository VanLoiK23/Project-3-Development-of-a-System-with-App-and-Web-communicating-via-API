package com.thuongmaidientu.dto;

import lombok.Data;

@Data
public class MomoDTO extends AbstractDTO<MomoDTO>{

    
    private String partnerCode;

    private Long orderId;

    private String amount;

    private String orderInfo;

    private String orderType;

    private Long transId;

    private String payType;

    private String codeCart;
}
