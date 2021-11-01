package io.wisoft.poomi.global.exception;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.exception.exceptions.BaseException;
import io.wisoft.poomi.global.exception.exceptions.FileNotReadableException;
import io.wisoft.poomi.global.exception.exceptions.NoPermissionOfContentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
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

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<ErrorResponse> unAuthentication() {
        return ApiResponse.failure(HttpStatus.UNAUTHORIZED,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.UN_AUTHENTICATION)
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<ErrorResponse> accessDenied() {
        return ApiResponse.failure(HttpStatus.FORBIDDEN,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.FORBIDDEN)
                        .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<ErrorResponse> notFound(NoHandlerFoundException e) {

        return ApiResponse.failure(HttpStatus.NOT_FOUND,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(BaseException.class)
    public ApiResponse<ErrorResponse> handleBaseException(final BaseException e) {
        return ApiResponse.failure(HttpStatus.valueOf(e.getErrorCode().getStatusCode()),
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

    @ExceptionHandler(FileNotReadableException.class)
    public ApiResponse<ErrorResponse> fileNotReadable(final FileNotReadableException ex) {
        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                        .errorCode(ex.getErrorCode())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<ErrorResponse> constraintViolation(final ConstraintViolationException ex) {

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.notHandlingFileType(ex.getMessage()))
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
    public ApiResponse<ErrorResponse> httpClientError(final HttpClientErrorException e) {
        log.error("소셜 로그인 요청 에러");
        log.error(e.getMessage());

        return ApiResponse.failure(HttpStatus.BAD_REQUEST,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.HTTP_CLIENT_ERROR)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<ErrorResponse> allException(final Exception e) {
        log.error("서버 측 에러");
        log.error("Error message: {}", e.getMessage());

        return ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorResponse.builder()
                        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                        .message(e.getMessage())
                        .build()
        );
    }

}