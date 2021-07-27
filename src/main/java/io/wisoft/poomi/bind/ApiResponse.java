package io.wisoft.poomi.bind;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private T data;
    private T error;

    private ApiResponse(final T data, final T error) {
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> succeed(T data) {
        return new ApiResponse<>(data, null);
    }

    public static <T> ApiResponse<T> failure(T error){
        return new ApiResponse<>(null, error);
    }

}
