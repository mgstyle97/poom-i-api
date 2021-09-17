package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class NotFoundEntityDataException extends BaseException {

    public NotFoundEntityDataException(final String message) {
        super(ErrorCode.notFound(message));
    }

}
