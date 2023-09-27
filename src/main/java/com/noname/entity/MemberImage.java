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
        name = "MEMBER_IMAGE_SEQ_GENERATOR",
        sequenceName = "MEMBER_IMAGE_SEQ",
        initialValue = 9,
        allocationSize = 1)
@Table(name = "member_image")
public class MemberImage extends BaseDateEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_IMAGE_SEQ_GENERATOR")
    private Long imageId;

    @Column(nullable = false, length = 15)
    private String path;

    @Column(nullable = false, length = 50)
    private String identifier;

    @Column(nullable = false, length = 50)
    private String name;

//    @Column(nullable = false)
//    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;






}
