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
        name = "FAVORITE_SEQ_GENERATOR",
        sequenceName = "FAVORITE_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "favorite")
public class Favorite extends BaseCreateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FAVORITE_SEQ_GENERATOR")
    private Long favoriteId;
//
//    @Column(nullable = false)
//    private Long productId;
//
//    @Column(nullable = false)
//    private Long memberId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;




}
