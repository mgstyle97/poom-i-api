package io.wisoft.poomi.common.error.exceptions;

public class NoPermissionOfClassProgram extends RuntimeException {

    private String message;

    public NoPermissionOfClassProgram(final String message) {
        super(message);
    }

}
