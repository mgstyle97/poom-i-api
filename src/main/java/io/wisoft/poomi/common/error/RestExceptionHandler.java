package io.wisoft.poomi.common.error;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.common.error.exceptions.DuplicateMemberException;
import io.wisoft.poomi.common.error.exceptions.WrongMemberPasswordException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<ErrorResponse> illegalArgument() {
        return ApiResponse.failure(ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Detected Using Illegal Argument")
                .build());
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ApiResponse<ErrorResponse> duplicateMember() {
        return ApiResponse
                .failure(ErrorResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .message("Duplicated member data")
                .build());
    }

    @ExceptionHandler(WrongMemberPasswordException.class)
    public ApiResponse<ErrorResponse> wrongPassword() {
        return ApiResponse
                .failure(ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("No match member id with input password")
                .build());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ApiResponse<ErrorResponse> typeMismatch() {
        return ApiResponse.failure(ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Type Mismatch")
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<ErrorResponse> invalidJSONFormatExp() {
        return ApiResponse.failure(ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Invalid JSON Format Request")
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<ErrorResponse> handleBindData(MethodArgumentNotValidException ex) {
        String errorCodes = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("\n"));
        return ApiResponse.failure(ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(errorCodes)
                .build());
    }
}
