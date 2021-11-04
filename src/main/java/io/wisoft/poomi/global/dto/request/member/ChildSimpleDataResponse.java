package io.wisoft.poomi.global.dto.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildSimpleDataResponse {

    @JsonProperty("child_id")
    private Long childId;

    private String name;

    @Builder
    public ChildSimpleDataResponse(final Long childId, final String name) {
        this.childId = childId;
        this.name = name;
    }

    public static ChildSimpleDataResponse of(final Child child) {
        return ChildSimpleDataResponse.builder()
                .childId(child.getId())
                .name(child.getName())
                .build();
    }

}
