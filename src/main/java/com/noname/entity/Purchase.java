package com.noname.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "PURCHASE_SEQ_GENERATOR",
        sequenceName = "PURCHASE_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "purchase")
public class Purchase extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PURCHASE_SEQ_GENERATOR")
    private Long purchaseId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long addressId;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false, length = 15)
    private String status;



//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "address_id")
//    private Address address;
//
//    @ManyToOne
//    @JoinColumn(name = "payment_id")
//    private Payment payment;



}
