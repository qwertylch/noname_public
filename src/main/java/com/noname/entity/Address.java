package com.noname.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import com.noname.enums.AddressType;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "ADDRESS_SEQ_GENERATOR",
        sequenceName = "ADDRESS_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "address")
public class Address extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ADDRESS_SEQ_GENERATOR")
    private Long addressId;


    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10)
    private Long phone;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false, length = 100)
    private String detail;

    @Column(nullable = false, length = 10)
    private Integer zipCode;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private AddressType status;
    
    @PrePersist
    public void prePersist(){
        status = AddressType.DISABLED;
    }

//    @Column(nullable = false)
//    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;




}
