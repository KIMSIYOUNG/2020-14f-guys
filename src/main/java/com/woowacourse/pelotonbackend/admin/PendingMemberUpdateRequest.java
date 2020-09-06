package com.woowacourse.pelotonbackend.admin;

import java.beans.ConstructorProperties;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = @ConstructorProperties("pendingCashIds"))
@Builder
@Getter
public class PendingMemberUpdateRequest {
    @NotNull
    private final List<Long> pendingCashIds;
}
