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
        name = "CHATTING_SEQ_GENERATOR",
        sequenceName = "CHATTING_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "chatting")
public class Chatting extends BaseCreateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "CHATTING_SEQ_GENERATOR")
    private Long chatId;

//    @Column(nullable = false)
//    private Long productId;
//
//    @Column(nullable = false)
//    private Long memberId;

    @Column(nullable = false, length = 500)
    private Long chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;




}
