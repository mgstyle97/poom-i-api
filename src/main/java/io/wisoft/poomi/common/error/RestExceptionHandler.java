package io.wisoft.poomi.common.error;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.utils.ErrorNotificationUtils;
import io.wisoft.poomi.common.error.exceptions.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.SocketException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler {

    private final ErrorNotificationUtils errorNotificationUtils;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<ErrorResponse> methodNotAllowed(HttpRequestMethodNotSupportedException e) {
        errorNotificationUtils.sendErrorInfo2Slack(e.getMessage());

        return ApiResponse.failure(HttpStatus.METHOD_NOT_ALLOWED, ErrorResponse.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorResponse> unauthorized() {
        errorNotificationUtils.sendErrorInfo2Slack("입력한 이메일과 패스워드가 일치하지 않습니다.");

        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                    .message("입력한 이메일과 패스워드가 일치하지 않습니다.")
                    .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<ErrorResponse> notFound(NoHandlerFoundException e) {
        errorNotificationUtils.sendErrorInfo2Slack(e.getMessage());

        return ApiResponse.failure(HttpStatus.NOT_FOUND, ErrorResponse.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<ErrorResponse> illegalArgument(IllegalArgumentException e) {
        errorNotificationUtils.sendErrorInfo2Slack(e.getMessage());

        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(DuplicateMemberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<ErrorResponse> duplicateMember() {
        errorNotificationUtils.sendErrorInfo2Slack("Duplicated member data");

        return ApiResponse
                .failure(HttpStatus.CONFLICT, ErrorResponse.builder()
                .message("Duplicated member data")
                .build());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ApiResponse<ErrorResponse> typeMismatch() {
        errorNotificationUtils.sendErrorInfo2Slack("Type mismatch");

        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message("Type Mismatch")
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<ErrorResponse> invalidJSONFormatExp() {
        errorNotificationUtils.sendErrorInfo2Slack("Invalid JSON Format Request");

        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message("Invalid JSON Format Request")
                .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ApiResponse<ErrorResponse> handleBindData(BindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        errorNotificationUtils.sendErrorInfo2Slack(errorMessage);

        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .message(errorMessage)
                .build());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiResponse<ErrorResponse> unsupportedMediaType(HttpMediaTypeNotSupportedException e) {
        errorNotificationUtils.sendErrorInfo2Slack(e.getMessage());

        return ApiResponse.failure(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ErrorResponse.builder()
                        .message(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(SocketException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<ErrorResponse> socketException() {
        errorNotificationUtils.sendErrorInfo2Slack("서버측 네트워크 연결이 불안정합니다. 잠시만 기다려주세요");

        return ApiResponse.failure(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorResponse.builder()
                        .message("서버측 네트워크 연결이 불안정합니다. 잠시만 기다려주세요")
                        .build()
        );
    }
}
