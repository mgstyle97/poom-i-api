package io.wisoft.poomi.exception.handler;

import io.wisoft.poomi.exception.InvalidApproachException;
import io.wisoft.poomi.exception.NotFoundException;
import io.wisoft.poomi.response.ApiResult;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ApiResult<?> notFound(final NotFoundException ex) {
        return ApiResult.get(HttpStatus.NOT_FOUND, null, ex.getMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ApiResult<?> typeMismatch(final TypeMismatchException ex) {
        return ApiResult.get(HttpStatus.UNSUPPORTED_MEDIA_TYPE, null, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult<?> invalidJsonFormat(final HttpMessageNotReadableException ex) {
        return ApiResult.get(HttpStatus.BAD_REQUEST, null, ex.getMessage());
    }

    @ExceptionHandler({InvalidApproachException.class, NullPointerException.class})
    public ApiResult<?> invalidApproach(final RuntimeException ex) {
        return ApiResult.get(HttpStatus.BAD_REQUEST, null, ex.getMessage());
    }

}
