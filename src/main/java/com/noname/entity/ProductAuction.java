package com.noname.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import com.noname.enums.AuctionStatus;
import com.noname.enums.ProductStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "PRODUCT_AUCTION_SEQ_GENERATOR",
        sequenceName = "PRODUCT_AUCTION_SEQ",
        initialValue = 81,
        allocationSize = 1)
@Table(name = "PRODUCT_AUCTION")
public class ProductAuction extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRODUCT_AUCTION_SEQ_GENERATOR")
    private Long auctionId;

    @Column(nullable = false)
    private Long startPrice;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false, length = 15)
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING) 
    private AuctionStatus status;
    
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = AuctionStatus.PENDING;
        }
    }

//    @Column(nullable = false)
//    private Long productId;


    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
