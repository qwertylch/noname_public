package com.noname.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    LISTED("결제 X"),
    PAID("결제 O"),
    CANCEL("결제 취소");

    private final String description;

}
