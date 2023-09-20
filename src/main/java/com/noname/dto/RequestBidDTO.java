package com.noname.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBidDTO {

    private String memberName;
    private String bidPrice;
    private String bidDate;

}
