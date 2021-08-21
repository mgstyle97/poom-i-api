package io.wisoft.poomi.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    @JsonProperty("status_code")
    private int statusCode;
    private T data;
    private T error;

    public ApiResponse(final HttpStatus httpStatus, final T data, final T error) {
        this.statusCode = httpStatus.value();
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> succeed(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(httpStatus, data, null);
    }

    public static <T> ApiResponse<T> failure(HttpStatus httpStatus, T error){
        return new ApiResponse<>(httpStatus, null, error);
    }

}
