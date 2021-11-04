package io.wisoft.poomi.global.dto.response.member.child;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildAddResponse {

    @JsonProperty("child_id")
    private Long childId;

    private String name;

    @Builder
    private ChildAddResponse(final Long childId, final String name) {
        this.childId = childId;
        this.name = name;
    }

    public static ChildAddResponse of(final Child child) {
        return ChildAddResponse.builder()
                .childId(child.getId())
                .name(child.getName())
                .build();
    }

}
