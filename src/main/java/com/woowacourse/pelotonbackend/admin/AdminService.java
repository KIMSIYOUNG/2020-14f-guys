package com.woowacourse.pelotonbackend.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    public Page<PendingMember> retrieveMemberWithPendingCash(Pageable pageable) {

        return adminRepository.findMembersByPendingCash(pageable);
    }
}
