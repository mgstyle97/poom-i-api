package io.wisoft.poomi.domain.member.dto;

import io.wisoft.poomi.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthInfo {

    private String email;
    private String nick;
    private String name;
    private LocalDateTime recentLoginAt;

    private AuthInfo(final Member member) {
        BeanUtils.copyProperties(member, this);
    }

    public static AuthInfo of(final Member member) {
        return new AuthInfo(member);
    }

}
