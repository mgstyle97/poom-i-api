package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class AlreadyRequestException extends BaseException {

    public AlreadyRequestException() {
        super(ErrorCode.ALREADY_REQUEST);
    }

}
