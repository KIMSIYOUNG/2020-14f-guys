package com.woowacourse.pelotonbackend.admin;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(adminRepository);
    }

    @Test
    void getPendingMembers() {
        Page<PendingMember> expected = AdminFixture.pendingMembers();
        when(adminService.retrieveMemberWithPendingCash(any(PageRequest.class))).thenReturn(expected);

        Page<PendingMember> pendingMembers = adminService.retrieveMemberWithPendingCash(AdminFixture.TEST_PENDING_PAGE);

        assertThat(pendingMembers).containsExactlyElementsOf(expected);
    }
}