package io.wisoft.poomi.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;

}
