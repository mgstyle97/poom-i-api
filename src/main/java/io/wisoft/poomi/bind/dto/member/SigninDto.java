package io.wisoft.poomi.bind.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SigninDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("login_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime loginAt;

    public static SigninDto of(String email, String accessToken) {
        SigninDto signinDto = new SigninDto();
        signinDto.setEmail(email);
        signinDto.setAccessToken(accessToken);
        signinDto.setLoginAt(LocalDateTime.now());

        return signinDto;
    }

}
