package io.wisoft.poomi.common.error;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.common.error.exceptions.DuplicateMemberException;
import io.wisoft.poomi.common.error.exceptions.WrongMemberPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<ErrorResponse> illegalArgument(IllegalArgumentException e) {
        log.error("Error message: {}", e.getMessage());

        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ApiResponse<ErrorResponse> duplicateMember() {
        return ApiResponse
                .failure(HttpStatus.CONFLICT, ErrorResponse.builder()
                .message("Duplicated member data")
                .build());
    }

    @ExceptionHandler(WrongMemberPasswordException.class)
    public ApiResponse<ErrorResponse> wrongPassword() {
        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message("No match member id with input password")
                .build());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ApiResponse<ErrorResponse> typeMismatch() {
        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message("Type Mismatch")
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<ErrorResponse> invalidJSONFormatExp() {
        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message("Invalid JSON Format Request")
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ErrorResponse> handleBindData(MethodArgumentNotValidException ex) {
        String errorCodes = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message(errorCodes)
                .build());
    }
}
