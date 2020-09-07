package com.woowacourse.pelotonbackend.admin;

import java.beans.ConstructorProperties;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.woowacourse.pelotonbackend.certification.domain.CertificationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = @ConstructorProperties({"ids", "status"}))
@Builder
@Getter
public class CertificationStatusUpdateRequests {
    @NotNull
    private final CertificationStatus status;

    @NotNull
    private final List<Long> ids;
}
