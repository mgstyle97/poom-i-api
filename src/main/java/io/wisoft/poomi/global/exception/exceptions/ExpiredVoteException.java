package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class ExpiredVoteException extends BaseException {

    public ExpiredVoteException() {
        super(ErrorCode.EXPIRED_VOTE);
    }

}
