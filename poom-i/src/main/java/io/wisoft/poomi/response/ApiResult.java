package io.wisoft.poomi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResult<T> {

    @JsonProperty("http_status")
    private HttpStatus httpStatus;
    private final T data;
    @JsonProperty("result_message")
    private final String resultMessage;

    private ApiResult(final HttpStatus httpStatus, final T data, final String resultMessage) {
        this.httpStatus = httpStatus;
        this.data = data;
        this.resultMessage = resultMessage;
    }

    public static <T> ApiResult<T> get(final HttpStatus httpStatus, final T data, final String resultMessage) {
        return new ApiResult<>(httpStatus, data, resultMessage);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "httpStatus=" + httpStatus +
                ", data=" + data +
                ", resultMessage='" + resultMessage + '\'' +
                '}';
    }
}
