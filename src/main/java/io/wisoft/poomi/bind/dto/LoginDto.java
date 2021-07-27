package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
public class LoginDto {

    @JsonProperty("login_id")
    private String loginId;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("login_at")
    private Date loginAt;

    public static LoginDto of(String loginId, String accessToken) {
        LoginDto loginDto = new LoginDto();
        loginDto.setLoginId(loginId);
        loginDto.setAccessToken(accessToken);
        loginDto.setLoginAt(new Date());

        return loginDto;
    }

}
