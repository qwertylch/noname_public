package com.noname.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayType {
    CARD("카드"),
    CASH("현금");

    private final String description;

}
