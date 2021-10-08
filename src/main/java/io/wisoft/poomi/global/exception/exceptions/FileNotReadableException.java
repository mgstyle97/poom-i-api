package io.wisoft.poomi.global.exception.exceptions;

import io.wisoft.poomi.global.exception.ErrorCode;

public class FileNotReadableException extends BaseException {

    public FileNotReadableException() {
        super(ErrorCode.FILE_NOT_READABLE);
    }

}
