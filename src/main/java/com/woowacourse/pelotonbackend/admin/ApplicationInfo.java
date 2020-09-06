package com.woowacourse.pelotonbackend.admin;

import java.beans.ConstructorProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = @ConstructorProperties({"riderCount", "raceCount"}))
@Getter
public class ApplicationInfo {
    private final Long riderCount;
    private final Long raceCount;
}
