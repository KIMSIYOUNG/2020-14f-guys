package com.woowacourse.pelotonbackend.member.domain;

public enum Role {
    MEMBER, ADMIN;

    public boolean isNotAdmin() {
        return !name().equals(ADMIN.name());
    }
}
