package io.wisoft.poomi.common.error.exceptions;

public class WrongMemberPasswordException extends RuntimeException {

    private String message;

    public WrongMemberPasswordException(String message) {
        super(message);
    }

}
