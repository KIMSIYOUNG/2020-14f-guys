package com.woowacourse.pelotonbackend.common.exception;

import com.woowacourse.pelotonbackend.common.ErrorCode;

public class PendingCashNotFoundException extends NotFoundException {
    public PendingCashNotFoundException(Long id) {
        super(ErrorCode.PENDING_CASH_NOT_FOUND, String.format("PendingCash(pending cash id = %d) does not exist)", id));
    }
}
