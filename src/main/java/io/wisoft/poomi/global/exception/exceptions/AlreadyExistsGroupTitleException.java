package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class AlreadyExistsGroupTitleException extends BaseException {

    public AlreadyExistsGroupTitleException() {
        super(ErrorCode.ALREADY_EXISTS_GROUP_TITLE);
    }

}
