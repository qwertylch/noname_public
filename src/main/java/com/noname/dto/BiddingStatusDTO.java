package com.noname.dto;

import com.noname.entity.Bidding;
import com.noname.enums.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiddingStatusDTO {
    private Bidding bidding;
    private AuctionStatus auctionStatus;
}
