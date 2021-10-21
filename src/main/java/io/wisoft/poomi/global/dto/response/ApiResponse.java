package io.wisoft.poomi.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.configures.security.jwt.JwtToken;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@ToString
@Getter
public class ApiResponse<T> extends ResponseEntity<T> {

    private final T data;
    private final T error;

    private final JwtToken tokenInfo;

    public ApiResponse(final HttpStatus httpStatus, HttpHeaders headers, final T data, final T error,
                       final JwtToken tokenInfo) {
        super((T) Body.of(data, error, tokenInfo), headers, httpStatus);
        this.data = data;
        this.error = error;
        this.tokenInfo = tokenInfo;
    }

    public static <T> ApiResponse<T> succeed(HttpStatus httpStatus, T data) {
        return succeedWithAccessToken(httpStatus, data, null);
    }

    public static <T> ApiResponse<T> succeedWithAccessToken(final HttpStatus httpStatus, final T data,
                                                            final JwtToken accessToken) {
        return createResponse(httpStatus, data, null, accessToken);
    }

    public static <T> ApiResponse<T> failure(HttpStatus httpStatus, T error){
        return createResponse(httpStatus, null, error, null);
    }

    private static <T> ApiResponse<T> createResponse(final HttpStatus httpStatus, final T data, final T error,
                                                     final JwtToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ApiResponse<>(httpStatus, headers, data, error, accessToken);
    }

    @Getter
    private static class Body<T> {

        private final T data;
        private final T error;

        @JsonProperty("token_info")
        private final JwtToken tokenInfo;

        private Body(final T data, final T error, final JwtToken tokenInfo) {
            this.data = data;
            this.error = error;
            this.tokenInfo = tokenInfo;
        }

        private static <T> Body of(final T data, final T error, final JwtToken tokenInfo) {
            return new Body(data, error, tokenInfo);
        }

    }

}
