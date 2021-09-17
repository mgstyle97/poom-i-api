package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class DuplicateMemberException extends BaseException{

    public DuplicateMemberException(final String message) {
        super(ErrorCode.duplicateMemberInfo(message));
    }

}
