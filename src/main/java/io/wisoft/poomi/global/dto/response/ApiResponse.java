package io.wisoft.poomi.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> extends ResponseEntity<T> {

    private final T data;
    private final T error;

    private final String accessToken;

    public ApiResponse(final HttpStatus httpStatus, final T data, final T error,
                       final String accessToken) {
        super((T) Body.of(data, error, accessToken), httpStatus);
        this.data = data;
        this.error = error;
        this.accessToken = accessToken;
    }

    public static <T> ApiResponse<T> succeed(HttpStatus httpStatus, T data) {
        return succeedWithAccessToken(httpStatus, data, null);
    }

    public static <T> ApiResponse<T> succeedWithAccessToken(final HttpStatus httpStatus, final T data,
                                                            final String accessToken) {
        return createResponse(httpStatus, data, null, accessToken);
    }

    public static <T> ApiResponse<T> failure(HttpStatus httpStatus, T error){
        return createResponse(httpStatus, null, error, null);
    }

    private static <T> ApiResponse<T> createResponse(final HttpStatus httpStatus, final T data, final T error,
                                                     final String accessToken) {
        return new ApiResponse<>(httpStatus, data, error, accessToken);
    }

    @Getter
    private static class Body<T> {

        private final T data;
        private final T error;

        @JsonProperty("access_token")
        private final String accessToken;

        private Body(final T data, final T error, final String accessToken) {
            this.data = data;
            this.error = error;
            this.accessToken = accessToken;
        }

        private static <T> Body of(final T data, final T error, final String accessToken) {
            return new Body(data, error, accessToken);
        }

    }

}
