package com.noname.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import com.noname.enums.ProductStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "PRODUCT_SEQ_GENERATOR",
        sequenceName = "PRODUCT_SEQ",
        initialValue = 81,
        allocationSize = 1)
@Table(name = "product")
public class Product extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRODUCT_SEQ_GENERATOR")
    private Long productId;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 1000)
    private String explanation;

    @Column(nullable = false, length = 15)
    private String type;

    @Column(nullable = false, length = 15)
    private String shape;

    @Column(nullable = false, length = 20)
    private String bulk;

    @Column(nullable = false, length = 15)
    private String color;

    @Column(nullable = false)
    private LocalDateTime mnfctDate;

    @Column(nullable = false, length = 15)
    @ColumnDefault("'PEDING'")
    @Enumerated(EnumType.STRING) 
    private ProductStatus status;
    
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = ProductStatus.PENDING;
        }
    }
    
//    @Column(nullable = false)
//    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER)
    @ToString.Exclude
    private ProductAuction productAuction;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Bidding> biddings = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Chatting> chattings = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<ProductReply> replies = new ArrayList<>();





}
