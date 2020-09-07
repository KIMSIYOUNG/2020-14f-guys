package com.woowacourse.pelotonbackend.admin;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woowacourse.pelotonbackend.certification.presentation.dto.CertificationResponses;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {
    private final AdminService adminService;

    @PutMapping("/pending")
    public ResponseEntity<Void> updateCashInfo(@Valid @RequestBody PendingMemberUpdateRequest request) {
        adminService.updateMemberCashes(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<PendingMember>> retrievePendingMembers(Pageable pageable) {

        return ResponseEntity.ok(adminService.retrieveMemberWithPendingCash(pageable));
    }

    @GetMapping("/appInfo")
    public ResponseEntity<ApplicationInfo> retrieveAppInfo() {

        return ResponseEntity.ok(adminService.retrieveAppInfo());
    }

    @GetMapping("/certifications")
    public ResponseEntity<CertificationResponses> retrieveCertifications(Pageable pageable) {

        CertificationResponses body = adminService.retrieveCertifications(pageable);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/certifications")
    public ResponseEntity<Void> updateCertificationsStatus(@Valid @RequestBody CertificationStatusUpdateRequests request) {
        adminService.updateCertificaitonsStatus(request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/certifications")
    public ResponseEntity<Void> deleteImproperCertifications(@Valid @RequestBody ImProperCertificationRequest request) {
        adminService.deleteCertificationByIds(request);

        return ResponseEntity.noContent().build();
    }
}
