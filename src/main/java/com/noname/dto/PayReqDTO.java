package com.noname.dto;

import com.noname.entity.Payment;
import com.noname.enums.PayType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayReqDTO {

    private String payType;
    private Long amount;
    private String orderName;
    private String customerName;
    private String orderId;

    public Payment toEntity() {
        return Payment.builder()
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(orderId)
                .build();
    }

}
