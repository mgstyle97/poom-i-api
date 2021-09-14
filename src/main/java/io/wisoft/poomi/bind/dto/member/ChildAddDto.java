package io.wisoft.poomi.bind.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.bind.request.member.ChildAddRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChildAddDto {

    @JsonProperty("child_id")
    private Long childId;

    private String name;

    @Builder
    private ChildAddDto(final Long childId, final String name) {
        this.childId = childId;
        this.name = name;
    }

    public static ChildAddDto of(final Child child) {
        return ChildAddDto.builder()
                .childId(child.getId())
                .name(child.getName())
                .build();
    }

}
