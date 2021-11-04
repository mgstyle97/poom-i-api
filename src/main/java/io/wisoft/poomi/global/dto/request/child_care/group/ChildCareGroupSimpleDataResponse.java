package io.wisoft.poomi.global.dto.request.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCareGroupSimpleDataResponse {

    @JsonProperty("group_id")
    private Long groupId;

    private String name;

    @Builder
    public ChildCareGroupSimpleDataResponse(final Long groupId, final String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public static ChildCareGroupSimpleDataResponse of(final ChildCareGroup group) {
        return ChildCareGroupSimpleDataResponse.builder()
                .groupId(group.getId())
                .name(group.getName())
                .build();
    }

}
