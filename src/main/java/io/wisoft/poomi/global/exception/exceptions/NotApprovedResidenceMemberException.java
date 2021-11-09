package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class NotApprovedResidenceMemberException extends BaseException {
    public NotApprovedResidenceMemberException() {
        super(ErrorCode.NOT_APPROVED_RESIDENCE);
    }
}
