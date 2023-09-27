package com.noname.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayResDTO {
    @JsonProperty("method")
    private String payType; // 결제 타입 - 카드/현금/포인트
    @JsonProperty("totalAmount")
    private Long amount; // 가격 정보
    private String orderId; // 주문 Id
    private String orderName; // 주문명
    private String customerAddress; // 고객 주소
    private String customerName; // 고객 이름
    private String successUrl; // 성공 시 리다이렉트 될 URL
    private String failUrl; // 실패 시 리다이렉트 될 URL⠀
    private String failReason; // 실패 이유
    private String cancelReason; // 취소 이유
    private String createdAt; // 결제가 이루어진 시간


}
