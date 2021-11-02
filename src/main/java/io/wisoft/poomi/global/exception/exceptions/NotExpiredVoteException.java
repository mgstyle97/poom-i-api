package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class NotExpiredVoteException extends BaseException {

    public NotExpiredVoteException() {
        super(ErrorCode.NOT_EXPIRED_VOTE);
    }

}
