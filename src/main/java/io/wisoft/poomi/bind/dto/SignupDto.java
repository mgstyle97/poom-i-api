package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
public class SignupDto {

    private String name;

    @JsonProperty("phone_number")
    private String phoneNumber;
    private String email;

    private String nick;

    @JsonProperty("joined_at")
    @JsonFormat(pattern = "yyyy.MM.dd-HH:mm:ss")
    private LocalDateTime joinedAt;

    public static SignupDto of(Member member) {
        SignupDto signupDto = new SignupDto();
        BeanUtils.copyProperties(member, signupDto);
        signupDto.setJoinedAt(LocalDateTime.now());

        return signupDto;
    }

}
