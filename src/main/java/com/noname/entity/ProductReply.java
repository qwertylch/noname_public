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
        name = "PRODUCT_REPLY_SEQ_GENERATOR",
        sequenceName = "PRODUCT_REPLY_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "product_reply")
public class ProductReply extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRODUCT_REPLY_SEQ_GENERATOR")
    private Long replyId;

//    @Column(nullable = false)
//    private Long productId;
//
//    @Column(nullable = false)
//    private Long memberId;

    @Column(nullable = false, length = 500)
    private String reply;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;




}
