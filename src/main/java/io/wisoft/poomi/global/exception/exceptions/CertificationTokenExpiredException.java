package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class CertificationTokenExpiredException extends BaseException{
    public CertificationTokenExpiredException() {
        super(ErrorCode.CERTIFICATION_TOKEN_EXPIRED);
    }
}
