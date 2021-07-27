package io.wisoft.poomi.bind.dto;

import io.wisoft.poomi.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
public class JoinDto {

    private String name;
    private int age;
    private String phoneNumber;
    private String email;
    private String nick;
    private LocalDateTime joinedAt;

    private JoinDto() {}

    public static JoinDto of(Member member) {
        JoinDto joinDto = new JoinDto();
        BeanUtils.copyProperties(member, joinDto);
        joinDto.setJoinedAt(LocalDateTime.now());

        return joinDto;
    }

}
