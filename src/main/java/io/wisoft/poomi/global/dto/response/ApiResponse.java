package io.wisoft.poomi.global.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> extends ResponseEntity<T> {

    private final T data;
    private final T error;

    public ApiResponse(final HttpStatus httpStatus, final T data, final T error) {
        super((T) Body.of(data, error), httpStatus);
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> succeed(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(httpStatus, data, null);
    }

    public static <T> ApiResponse<T> failure(HttpStatus httpStatus, T error){
        return new ApiResponse<>(httpStatus, null, error);
    }

    @Getter
    private static class Body<T> {

        private final T data;
        private final T error;

        private Body(final T data, final T error) {
            this.data = data;
            this.error = error;
        }

        private static <T> Body of(final T data, final T error) {
            return new Body(data, error);
        }

    }

}
