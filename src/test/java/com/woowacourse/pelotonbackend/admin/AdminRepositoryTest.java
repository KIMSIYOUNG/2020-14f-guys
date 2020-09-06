package com.woowacourse.pelotonbackend.admin;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestExecutionListeners;

import com.woowacourse.pelotonbackend.DataInitializeExecutionListener;
import com.woowacourse.pelotonbackend.member.domain.Member;
import com.woowacourse.pelotonbackend.member.domain.MemberFixture;
import com.woowacourse.pelotonbackend.member.domain.MemberRepository;
import com.woowacourse.pelotonbackend.pendingcash.PendingCashRepository;
import com.woowacourse.pelotonbackend.pendingcash.PendingFixture;

@SpringBootTest
@TestExecutionListeners(
    listeners = DataInitializeExecutionListener.class,
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PendingCashRepository pendingCashRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("여러개의 pendingMember를 조회한다.")
    @Test
    void findPendingMembers() {
        Member firstMember = memberRepository.save(
            MemberFixture.createWithoutId(MemberFixture.KAKAO_ID, MemberFixture.EMAIL, MemberFixture.NAME));
        Member secondMember = memberRepository.save(
            MemberFixture.createWithoutId(MemberFixture.KAKAO_ID2, MemberFixture.EMAIL2, MemberFixture.NAME2));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(secondMember.getId()));

        Page<PendingMember> result = adminRepository.findMembersByPendingCash(AdminFixture.TEST_PENDING_PAGE);

        assertAll(
            () -> assertThat(result.getContent()).hasSize(3),
            () -> assertThat(result.getTotalPages()).isEqualTo(2),
            () -> assertThat(result.getTotalElements()).isEqualTo(4),
            () -> assertThat(result.getPageable().getPageSize()).isEqualTo(3)
        );
    }

    @DisplayName("pendingMember가 한명인 경우에도 정상 조회된다.")
    @Test
    void findPendingMember() {
        Member firstMember = memberRepository.save(
            MemberFixture.createWithoutId(MemberFixture.KAKAO_ID, MemberFixture.EMAIL, MemberFixture.NAME));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));

        Page<PendingMember> result = adminRepository.findMembersByPendingCash(AdminFixture.TEST_PENDING_PAGE);

        assertAll(
            () -> assertThat(result.getContent()).hasSize(1),
            () -> assertThat(result.getTotalPages()).isEqualTo(1),
            () -> assertThat(result.getTotalElements()).isEqualTo(1),
            () -> assertThat(result.getPageable().getPageSize()).isEqualTo(3)
        );
    }

    @DisplayName("pendingMember가 없는 경우 빈 리스트를 반환한다.")
    @Test
    void findEmptyPendingMember() {
        Page<PendingMember> result = adminRepository.findMembersByPendingCash(AdminFixture.TEST_PENDING_PAGE);

        assertAll(
            () -> assertThat(result.getContent()).hasSize(0),
            () -> assertThat(result.getTotalPages()).isEqualTo(0),
            () -> assertThat(result.getTotalElements()).isEqualTo(0)
            );
    }
}