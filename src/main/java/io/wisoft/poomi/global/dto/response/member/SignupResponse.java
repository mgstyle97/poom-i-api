package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
public class SignupResponse {

    private String name;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    private String nick;

    @JsonProperty("joined_at")
    @JsonFormat(pattern = "yyyy.MM.dd-HH:mm:ss")
    private LocalDateTime joinedAt;

    public static SignupResponse of(Member member) {
        SignupResponse signupResponse = new SignupResponse();
        BeanUtils.copyProperties(member, signupResponse);
        signupResponse.setJoinedAt(LocalDateTime.now());

        return signupResponse;
    }

}
