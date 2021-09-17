package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class NoMatchCertificationNumberException extends BaseException {

    public NoMatchCertificationNumberException() {
        super(ErrorCode.NO_MATCH_DATA);
    }

}
