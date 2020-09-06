package com.woowacourse.pelotonbackend.admin;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

import com.woowacourse.pelotonbackend.pendingcash.CashStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = @ConstructorProperties({"id","memberId", "name","email","cash","status"}))
@Getter
@ToString
public class PendingMember {
    private final Long id;
    private final Long memberId;
    private final String name;
    private final String email;
    private final BigDecimal cash;
    private final CashStatus status;
}
