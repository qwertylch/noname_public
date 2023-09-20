package com.noname.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressType {
    ACTIVE("기본배송지로 저장시"),
    DISABLED("그 외 배송지들"),
    DELETED("삭제된 주소");

    private final String description;

}
