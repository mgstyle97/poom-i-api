package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SigninDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("login_at")
    private Date loginAt;

    public static SigninDto of(String email, String accessToken) {
        SigninDto signinDto = new SigninDto();
        signinDto.setEmail(email);
        signinDto.setAccessToken(accessToken);
        signinDto.setLoginAt(new Date());

        return signinDto;
    }

}
