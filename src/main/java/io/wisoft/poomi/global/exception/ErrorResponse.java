package io.wisoft.poomi.global.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    @JsonProperty("error_code")
    private ErrorCode errorCode;

    @JsonProperty("status_code")
    private int statusCode;

    private String message;

    @Builder
    public ErrorResponse(final ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
    }

}
