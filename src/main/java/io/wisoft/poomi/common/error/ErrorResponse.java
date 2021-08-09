package io.wisoft.poomi.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponse {

    private String message;

}
