package io.wisoft.poomi.global.exception;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.exception.exceptions.BaseException;
import io.wisoft.poomi.global.exception.exceptions.NoPermissionOfContentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.SocketException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({NumberFormatException.class})
    public ApiResponse<ErrorResponse> invalidParameterFormat() {
        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_FORMAT)
                .build()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<ErrorResponse> methodNotAllowed(HttpRequestMethodNotSupportedException e) {

        return ApiResponse.failure(HttpStatus.METHOD_NOT_ALLOWED,
                ErrorResponse.builder()
                .errorCode(ErrorCode.METHOD_NOT_ALLOWED)
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<ErrorResponse> badCredentials() {

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                .errorCode(ErrorCode.BAD_CREDENTIALS)
                .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<ErrorResponse> notFound(NoHandlerFoundException e) {

        return ApiResponse.failure(HttpStatus.NOT_FOUND,
                ErrorResponse.builder()
                .errorCode(ErrorCode.NOT_FOUND)
                .build());
    }

    @ExceptionHandler(NoPermissionOfContentException.class)
    public ApiResponse<ErrorResponse> noPermission() {
        return ApiResponse.failure(HttpStatus.FORBIDDEN,
                ErrorResponse.builder()
                .errorCode(ErrorCode.NO_PERMISSION)
                .build()
        );
    }

    @ExceptionHandler(BaseException.class)
    public ApiResponse<ErrorResponse> handleBaseException(final BaseException e) {
        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<ErrorResponse> illegalArgument(IllegalArgumentException e) {

        ErrorCode errorCode = ErrorCode.illegalArgument(e.getMessage());

        return ApiResponse
                .failure(HttpStatus.BAD_REQUEST, ErrorResponse.builder()
                .errorCode(errorCode)
                .build());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ApiResponse<ErrorResponse> typeMismatch() {

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                .errorCode(ErrorCode.TYPE_MISMATCH)
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<ErrorResponse> invalidJSONFormatExp() {

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_JSON_FORMAT)
                .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ApiResponse<ErrorResponse> handleBindData(BindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                .errorCode(ErrorCode.argumentNotValid(errorMessage))
                .build());
    }

    @ExceptionHandler(FileUploadException.class)
    public ApiResponse<ErrorResponse> fileUpload(final FileUploadException ex) {

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.FILE_UPLOAD_FAILED)
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResponse<ErrorResponse> unsupportedMediaType(HttpMediaTypeNotSupportedException e) {

        return ApiResponse.failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.NOT_SUPPORTED_MEDIA_TYPE)
                        .build()
        );
    }

    @ExceptionHandler(SocketException.class)
    public ApiResponse<ErrorResponse> socketException() {

        return ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                        .build()
        );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ApiResponse<ErrorResponse> httpClientError() {
        log.error("소셜 로그인 요청 에러");

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.HTTP_CLIENT_ERROR)
                        .build()
        );
    }

}