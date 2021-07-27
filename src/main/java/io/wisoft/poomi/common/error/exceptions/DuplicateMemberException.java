package io.wisoft.poomi.common.error.exceptions;

public class DuplicateMemberException extends RuntimeException{

    private String message;

    public DuplicateMemberException(final String message) {
        super(message);
    }

}
