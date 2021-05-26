package io.wisoft.poomi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class MemberResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("nick")
    private String nick;

    private MemberResponse(final Member member) {
        BeanUtils.copyProperties(member, this);
    }

    public static MemberResponse of(final Member member) {
        return new MemberResponse(member);
    }

}
