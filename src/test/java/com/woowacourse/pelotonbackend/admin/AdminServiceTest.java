package com.woowacourse.pelotonbackend.admin;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.woowacourse.pelotonbackend.race.domain.RaceFixture;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(adminRepository);
    }

    @DisplayName("현재 대기중인 충전 금액들을 모두 불러온다.")
    @Test
    void getPendingMembers() {
        Page<PendingMember> expected = AdminFixture.pendingMembers();
        when(adminService.retrieveMemberWithPendingCash(any(PageRequest.class))).thenReturn(expected);

        Page<PendingMember> pendingMembers = adminService.retrieveMemberWithPendingCash(AdminFixture.TEST_PENDING_PAGE);

        assertThat(pendingMembers).containsExactlyElementsOf(expected);
    }

    @DisplayName("요청 받은 캐쉬를 충전한다.")
    @Test
    void updateCashes() {
        when(adminRepository.findPendingCashes(any(List.class))).thenReturn(AdminFixture.pendingCashes());

        adminService.updateMemberCashes(AdminFixture.createPendingCashUpdateIds());

        verify(adminRepository, times(3)).updateMembersCash(any(Long.class), any(BigDecimal.class));
        verify(adminRepository, times(1)).updatePendingStatuses(any(List.class));
    }

    @DisplayName("어플리케이션 상태정보를 조회한다.(진행중인 레이스와 참여 인원들)")
    @Test
    void retrieveAppInfo() {
        when(adminRepository.findCountActiveRaces()).thenReturn(AdminFixture.TEST_RACE_COUNT);
        when(adminRepository.findCountActiveRiders()).thenReturn(AdminFixture.TEST_RIDER_COUNT);

        ApplicationInfo applicationInfo = adminService.retrieveAppInfo();

        assertThat(applicationInfo).isEqualToComparingFieldByField(AdminFixture.createAppInfo());
    }
}