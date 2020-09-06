package com.woowacourse.pelotonbackend.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.pelotonbackend.pendingcash.CashStatus;
import com.woowacourse.pelotonbackend.pendingcash.PendingCash;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
class AdminService {
    private final AdminRepository adminRepository;

    void updateMemberCashes(PendingMemberUpdateRequest request) {
        List<Long> ids = request.getPendingCashIds();
        List<PendingCash> pendingCashes = adminRepository.findPendingCashes(ids);

        pendingCashes.forEach(pendingCash -> pendingCash.updateStatus(CashStatus.RESOLVE));
        adminRepository.updatePendingStatuses(ids);
        pendingCashes.forEach(pendingCash ->
            adminRepository.updateMembersCash(pendingCash.getMemberId().getId(), pendingCash.getCash().getCash()));
    }

    @Transactional(readOnly = true)
    Page<PendingMember> retrieveMemberWithPendingCash(Pageable pageable) {

        return adminRepository.findMembersByPendingCash(pageable);
    }

    @Transactional(readOnly = true)
    ApplicationInfo retrieveAppInfo() {

        return ApplicationInfo.builder()
            .riderCount(adminRepository.findCountActiveRiders())
            .raceCount(adminRepository.findCountActiveRaces())
            .build();
    }
}
