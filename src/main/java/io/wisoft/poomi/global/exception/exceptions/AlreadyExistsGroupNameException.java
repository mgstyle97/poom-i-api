package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class AlreadyExistsGroupNameException extends BaseException {

    public AlreadyExistsGroupNameException() {
        super(ErrorCode.ALREADY_EXISTS_GROUP_NAME);
    }

}
