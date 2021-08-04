package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChildAddDto {

    @JsonProperty("login_id")
    private String loginId;

    private List<Child> children;

    public static ChildAddDto of(Member member) {
        ChildAddDto childAddDto = new ChildAddDto();
        childAddDto.setLoginId(member.getLoginId());
        childAddDto.setChildren(member.getChildren());

        return childAddDto;
    }

}
