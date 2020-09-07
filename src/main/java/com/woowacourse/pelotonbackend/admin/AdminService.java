package com.woowacourse.pelotonbackend.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.pelotonbackend.certification.domain.Certification;
import com.woowacourse.pelotonbackend.certification.presentation.dto.CertificationResponses;
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

    @Transactional(readOnly = true)
    CertificationResponses retrieveCertifications(Pageable pageable) {
        Page<Certification> certifications = adminRepository.findAllCertifications(pageable);

        return CertificationResponses.of(certifications);
    }

    void updateCertificaitonsStatus(CertificationStatusUpdateRequests request) {
        adminRepository.updateCertifications(request.getIds(), request.getStatus());
    }

    void deleteCertificationByIds(ImProperCertificationRequest request) {
        List<Long> improperCertificationIds = request.getIds();
        adminRepository.deleteCertificationByIds(improperCertificationIds);
    }
}
