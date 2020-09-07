package com.woowacourse.pelotonbackend.admin;

import static com.woowacourse.pelotonbackend.admin.AdminFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestExecutionListeners;

import com.woowacourse.pelotonbackend.DataInitializeExecutionListener;
import com.woowacourse.pelotonbackend.certification.domain.Certification;
import com.woowacourse.pelotonbackend.certification.domain.CertificationFixture;
import com.woowacourse.pelotonbackend.certification.domain.CertificationRepository;
import com.woowacourse.pelotonbackend.member.domain.Member;
import com.woowacourse.pelotonbackend.member.domain.MemberFixture;
import com.woowacourse.pelotonbackend.member.domain.MemberRepository;
import com.woowacourse.pelotonbackend.pendingcash.CashStatus;
import com.woowacourse.pelotonbackend.pendingcash.PendingCash;
import com.woowacourse.pelotonbackend.pendingcash.PendingCashRepository;
import com.woowacourse.pelotonbackend.pendingcash.PendingFixture;
import com.woowacourse.pelotonbackend.race.domain.Race;
import com.woowacourse.pelotonbackend.race.domain.RaceFixture;
import com.woowacourse.pelotonbackend.race.domain.RaceRepository;
import com.woowacourse.pelotonbackend.rider.domain.RiderFixture;
import com.woowacourse.pelotonbackend.rider.domain.RiderRepository;

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

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private CertificationRepository certificationRepository;

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

    @DisplayName("pendingCash에 대해서 findAllByIds() 를 수행할 수 있다.")
    @Test
    void findPendingCashesByIds() {
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(MemberFixture.MEMBER_ID));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(MemberFixture.MEMBER_ID));

        List<PendingCash> cashes = adminRepository.findPendingCashes(Arrays.asList(1L, 2L));
        assertThat(cashes).hasSize(2);
    }

    @DisplayName("Pending상태를 정상적으로 수정한다.")
    @Test
    void updatePendingCash() {
        Member firstMember = memberRepository.save(
            MemberFixture.createWithoutId(MemberFixture.KAKAO_ID, MemberFixture.EMAIL, MemberFixture.NAME));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));

        adminRepository.updatePendingStatuses(Lists.newArrayList(1L, 2L));
        Iterable<PendingCash> pendingCashes = pendingCashRepository.findAll();

        pendingCashes.forEach((cash) -> assertThat(cash.getCashStatus()).isEqualTo(CashStatus.RESOLVE));
    }

    @DisplayName("회원의 캐시를 정상적으로 업데이트 한다.")
    @Test
    void updateMemberCashes() {
        Member firstMember = memberRepository.save(
            MemberFixture.createWithoutId(MemberFixture.KAKAO_ID, MemberFixture.EMAIL, MemberFixture.NAME));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));
        pendingCashRepository.save(PendingFixture.createWithOutIdWithMemberId(firstMember.getId()));

        adminRepository.updateMembersCash(firstMember.getId(), TEST_CASH.getCash());
        Member member = memberRepository.findById(firstMember.getId()).get();

        assertThat(member.getCash()).isEqualTo(TEST_CASH.plus(MemberFixture.CASH));
    }

    @DisplayName("현재 달리고 있는 라이더들의 인원수를 조회한다."
        + "현재 진행중인 레이스 1개, 참가 인원 2명"
        + "이미 끝난 레이스 1개, 참가 인원 2명")
    @Test
    void countActiveRiders() {
        raceRepository.save(RaceFixture.createWithoutId());
        riderRepository.save(RiderFixture.createRiderWithoutId());
        riderRepository.save(RiderFixture.createRiderWithoutId());

        Race nonActiveRace = raceRepository.save(RaceFixture.createNonActiveWithoutId());
        riderRepository.save(RiderFixture.createRiderWithIdAndRaceId(null, nonActiveRace.getId()));
        riderRepository.save(RiderFixture.createRiderWithIdAndRaceId(null, nonActiveRace.getId()));

        Long count = adminRepository.findCountActiveRiders();

        assertThat(count).isEqualTo(2);
    }

    @DisplayName("현재 저장되어 있는 인증 사진을 조회한다.")
    @Test
    void findCertifications() {
        certificationRepository.save(CertificationFixture.createCertificationWithoutId());
        certificationRepository.save(CertificationFixture.createCertificationWithoutId());

        Page<Certification> certifications = adminRepository.findAllCertifications(TEST_CERTIFICATION_PAGE);

        assertThat(certifications.getContent()).hasSize(2);
    }

    @DisplayName("특정 사진의 상태를 변경한다.")
    @Test
    void updateCertifications() {
        certificationRepository.save(CertificationFixture.createCertificationWithoutId());
        certificationRepository.save(CertificationFixture.createCertificationWithoutId());

        adminRepository.updateCertifications(TEST_FAIL_CERTIFICATION_IDS, TEST_CERTIFICATION_FAIL);
        Page<Certification> results = adminRepository.findAllCertifications(TEST_CERTIFICATION_PAGE);

        long count = results.getContent().stream()
            .filter(certification -> Objects.equals(certification.getStatus(), TEST_CERTIFICATION_FAIL))
            .count();

        assertThat(TEST_FAIL_CERTIFICATION_IDS.size()).isEqualTo(count);
    }

    @DisplayName("특정 인증을 삭제한다.")
    @Test
    void deleteCertifications() {
        certificationRepository.save(CertificationFixture.createCertificationWithoutId());
        certificationRepository.save(CertificationFixture.createCertificationWithoutId());

        adminRepository.deleteCertificationByIds(TEST_FAIL_CERTIFICATION_IDS);
        Page<Certification> results = adminRepository.findAllCertifications(TEST_CERTIFICATION_PAGE);

        assertThat(results.getContent()).hasSize(2 - TEST_FAIL_CERTIFICATION_IDS.size());
    }
}