package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class NoPermissionOfContentException extends BaseException {
    public NoPermissionOfContentException() {
        super(ErrorCode.NO_PERMISSION);
    }
}
