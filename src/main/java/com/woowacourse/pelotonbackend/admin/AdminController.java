package com.woowacourse.pelotonbackend.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/pending")
    public ResponseEntity<Page<PendingMember>> retrievePendingMembers(Pageable pageable) {

        return ResponseEntity.ok(adminService.retrieveMemberWithPendingCash(pageable));
    }
}
