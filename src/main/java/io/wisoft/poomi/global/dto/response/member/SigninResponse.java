package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SigninResponse {

    @JsonProperty("email")
    private String email;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("login_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime loginAt;

    public static SigninResponse of(String email, String accessToken) {
        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setEmail(email);
        signinResponse.setAccessToken(accessToken);
        signinResponse.setLoginAt(LocalDateTime.now());

        return signinResponse;
    }

}
