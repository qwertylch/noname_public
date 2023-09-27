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
        name = "BIDDING_SEQ_GENERATOR",
        sequenceName = "BIDDING_SEQ",
        initialValue = 12,
        allocationSize = 1)
@Table(name = "bidding")
public class Bidding extends BaseCreateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "BIDDING_SEQ_GENERATOR")
    private Long bidId;

//    @Column(nullable = false)
//    private Long productId;
//
//    @Column(nullable = false)
//    private Long memberId;

    @Column(nullable = false)
    private Integer bidPrice;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;




}
