package com.woowacourse.pelotonbackend.admin;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.woowacourse.pelotonbackend.pendingcash.PendingCash;
import com.woowacourse.pelotonbackend.pendingcash.PendingFixture;
import com.woowacourse.pelotonbackend.vo.Cash;

public class AdminFixture {
    public static final String TEST_PENDING_NAME = "TEST_NAME";
    public static final String TEST_PENDING_EMAIL = "TEST_NAME@TEST_EMAIL.com";
    public static final Cash TEST_CASH = Cash.of(1000000);
    public static final List<PendingMember> TEST_PENDING_MEMBERS = Lists.newArrayList(
        pendingMember(1L),
        pendingMember(2L),
        pendingMember(3L)
    );
    public static final PageRequest TEST_PENDING_PAGE = PageRequest.of(0, 3);
    public static final List<Long> TEST_PENDING_CASH_IDS = Lists.newArrayList(1L, 2L);
    public static final Long TEST_RACE_COUNT = 2L;
    public static final Long TEST_RIDER_COUNT = 6L;


    public static Page<PendingMember> pendingMembers() {
        return PageableExecutionUtils.getPage(
            TEST_PENDING_MEMBERS,
            TEST_PENDING_PAGE,
            TEST_PENDING_MEMBERS::size);
    }

    public static List<PendingCash> pendingCashes() {
        return Lists.newArrayList(
            PendingFixture.createWithId(1L),
            PendingFixture.createWithId(2L),
            PendingFixture.createWithId(3L)
        );
    }

    public static PendingMember pendingMember(Long id) {
        return PendingMember.builder()
            .id(id)
            .name(TEST_PENDING_NAME)
            .email(TEST_PENDING_EMAIL)
            .cash(BigDecimal.valueOf(10000))
            .build();
    }

    public static PendingMemberUpdateRequest createPendingCashUpdateIds() {
        return PendingMemberUpdateRequest.builder()
            .pendingCashIds(TEST_PENDING_CASH_IDS)
            .build();
    }

    public static ApplicationInfo createAppInfo() {
        return ApplicationInfo.builder()
            .raceCount(TEST_RACE_COUNT)
            .riderCount(TEST_RIDER_COUNT)
            .build();
    }
}
