package com.woowacourse.pelotonbackend.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.woowacourse.pelotonbackend.certification.domain.Certification;
import com.woowacourse.pelotonbackend.certification.domain.CertificationFixture;
import com.woowacourse.pelotonbackend.certification.domain.CertificationStatus;
import com.woowacourse.pelotonbackend.certification.presentation.dto.CertificationResponses;
import com.woowacourse.pelotonbackend.pendingcash.PendingCash;
import com.woowacourse.pelotonbackend.pendingcash.PendingFixture;
import com.woowacourse.pelotonbackend.vo.Cash;

public class AdminFixture {
    public static final String TEST_PENDING_NAME = "TEST_NAME";
    public static final String TEST_PENDING_EMAIL = "TEST_NAME@TEST_EMAIL.com";
    public static final Cash TEST_CASH = Cash.of(1000000);
    public static final List<PendingMember> TEST_PENDING_MEMBERS = Lists.newArrayList(
        createPendingMember(1L),
        createPendingMember(2L),
        createPendingMember(3L)
    );
    public static final PageRequest TEST_PENDING_PAGE = PageRequest.of(0, 3);
    public static final PageRequest TEST_CERTIFICATION_PAGE = PageRequest.of(0, 10);
    public static final List<Long> TEST_PENDING_CASH_IDS = Lists.newArrayList(1L, 2L);
    public static final Long TEST_RACE_COUNT = 2L;
    public static final Long TEST_RIDER_COUNT = 6L;
    public static final CertificationStatus TEST_CERTIFICATION_FAIL = CertificationStatus.FAIL;
    public static final List<Long> TEST_FAIL_CERTIFICATION_IDS = Lists.newArrayList(1L, 2L);
    public static final List<Long> TEST_IMPROPER_CERTIFICATION_IDS = Lists.newArrayList(3L, 4L);

    public static Page<PendingMember> createPendingMembers() {
        return PageableExecutionUtils.getPage(
            TEST_PENDING_MEMBERS,
            TEST_PENDING_PAGE,
            TEST_PENDING_MEMBERS::size);
    }

    public static List<PendingCash> createPendingCashes() {
        return Lists.newArrayList(
            PendingFixture.createWithId(1L),
            PendingFixture.createWithId(2L),
            PendingFixture.createWithId(3L)
        );
    }

    public static PendingMember createPendingMember(Long id) {
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

    public static CertificationStatusUpdateRequests createCertificationUpdateRequests() {
        return CertificationStatusUpdateRequests.builder()
            .status(TEST_CERTIFICATION_FAIL)
            .ids(TEST_FAIL_CERTIFICATION_IDS)
            .build();
    }

    public static ImProperCertificationRequest createImproperCertificationRequests() {
        return ImProperCertificationRequest.builder()
            .ids(TEST_IMPROPER_CERTIFICATION_IDS)
            .build();
    }

    public static CertificationResponses createCertificationResponses() {
        return CertificationResponses.of(
            new PageImpl<>(createCertifications(), TEST_CERTIFICATION_PAGE, createCertifications().size()));
    }

    public static List<Certification> createCertifications() {
        return LongStream.rangeClosed(1, 6)
            .mapToObj(CertificationFixture::createCertificationWithId)
            .collect(Collectors.toList());
    }

    public static Page<Certification> createCertificationsWithPage() {
        List<Certification> content = createCertifications();

        return new PageImpl<>(content, TEST_CERTIFICATION_PAGE, content.size());
    }
}
