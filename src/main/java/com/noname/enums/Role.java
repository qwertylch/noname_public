package com.noname.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.PrePersist;

// 사용자 권한 enum
@RequiredArgsConstructor
@Getter
public enum Role {
    MEMBER("ROLE_MEMBER"), ADMIN("ROLE_ADMIN");
    private final String value;

}