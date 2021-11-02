package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SigninResponse {

    private String email;

    private String nick;

    @JsonProperty("login_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime loginAt;

    @Builder
    public SigninResponse(final String email, final String nick) {
        this.email = email;
        this.nick = nick;
        this.loginAt = LocalDateTime.now();
    }

    public static SigninResponse of(final Member member) {
        return SigninResponse.builder()
                .email(member.getEmail())
                .nick(member.getNick())
                .build();
    }

}
