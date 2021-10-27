package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class NotApprovedVoteException extends BaseException {

    public NotApprovedVoteException() {
        super(ErrorCode.NOT_APPROVED_VOTE);
    }

}
