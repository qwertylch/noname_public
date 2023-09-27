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
        name = "PRODUCT_IMAGE_SEQ_GENERATOR",
        sequenceName = "PRODUCT_IMAGE_SEQ",
        initialValue = 218,
        allocationSize = 1)
@Table(name = "PRODUCT_IMAGE")
public class ProductImage extends BaseDateEntity{

    public ProductImage(String path, String identifier, String name) {
        this.path = path;
        this.identifier = identifier;
        this.name = name;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRODUCT_IMAGE_SEQ_GENERATOR")
    private Long imageId;

    @Column(nullable = false, length = 15)
    private String path;

    @Column(nullable = false, length = 50)
    private String identifier;

    @Column(nullable = false, length = 50)
    private String name;

//    @Column(nullable = false)
//    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;






}
