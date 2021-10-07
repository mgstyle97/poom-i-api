package io.wisoft.poomi.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.configures.security.jwt.JWTToken;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> extends ResponseEntity<T> {

    private final T data;
    private final T error;

    private final JWTToken tokenInfo;

    public ApiResponse(final HttpStatus httpStatus, final T data, final T error,
                       final JWTToken tokenInfo) {
        super((T) Body.of(data, error, tokenInfo), httpStatus);
        this.data = data;
        this.error = error;
        this.tokenInfo = tokenInfo;
    }

    public static <T> ApiResponse<T> succeed(HttpStatus httpStatus, T data) {
        return succeedWithAccessToken(httpStatus, data, null);
    }

    public static <T> ApiResponse<T> succeedWithAccessToken(final HttpStatus httpStatus, final T data,
                                                            final JWTToken accessToken) {
        return createResponse(httpStatus, data, null, accessToken);
    }

    public static <T> ApiResponse<T> failure(HttpStatus httpStatus, T error){
        return createResponse(httpStatus, null, error, null);
    }

    private static <T> ApiResponse<T> createResponse(final HttpStatus httpStatus, final T data, final T error,
                                                     final JWTToken accessToken) {
        return new ApiResponse<>(httpStatus, data, error, accessToken);
    }

    @Getter
    private static class Body<T> {

        private final T data;
        private final T error;

        @JsonProperty("token_info")
        private final JWTToken tokenInfo;

        private Body(final T data, final T error, final JWTToken tokenInfo) {
            this.data = data;
            this.error = error;
            this.tokenInfo = tokenInfo;
        }

        private static <T> Body of(final T data, final T error, final JWTToken tokenInfo) {
            return new Body(data, error, tokenInfo);
        }

    }

}
