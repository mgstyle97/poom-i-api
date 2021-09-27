package io.wisoft.poomi.global.dto.response.oauth.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverUserInfoResponse {

    @JsonProperty("resultcode")
    private String resultCode;

    private String message;

    private Response response;

    @Getter
    @Setter
    public static class Response {

        private String id;
        private String name;
        private String email;

    }

}
