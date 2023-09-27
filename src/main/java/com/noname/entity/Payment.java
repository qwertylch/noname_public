package com.noname.entity;

import com.noname.dto.PayResDTO;
import com.noname.enums.AddressType;
import com.noname.enums.OrderStatus;
import com.noname.enums.PayType;
import lombok.*;
import org.hibernate.criterion.Order;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "PAYMENT_SEQ_GENERATOR",
        sequenceName = "PAYMENT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "payment")
public class Payment extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PAYMENT_SEQ_GENERATOR")
    private Long paymentId;

    @Column
    private Long amount;

    @Column
    private String orderId;

    @Column
    private String orderName;

    @Column
    private String customerName;

    @Column
    private String customerAddress;

    @Column
    private String payType;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    public PayResDTO toPayResDTO() {
        return PayResDTO.builder()
                .payType(payType)
                .amount(amount)
                .customerName(customerName)
                .customerAddress(customerAddress)
                .orderId(orderId)
                .orderName(orderName)
                .build();
    }







}
