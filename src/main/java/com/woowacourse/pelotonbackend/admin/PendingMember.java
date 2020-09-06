package com.woowacourse.pelotonbackend.admin;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.woowacourse.pelotonbackend.pendingcash.CashStatus;
import com.woowacourse.pelotonbackend.vo.Cash;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = @ConstructorProperties({"id","name","email","cash","status"}))
@Getter
@ToString
public class PendingMember {
    private final Long id;
    private final String name;
    private final String email;
    private final BigDecimal cash;
    private final CashStatus status;
}
