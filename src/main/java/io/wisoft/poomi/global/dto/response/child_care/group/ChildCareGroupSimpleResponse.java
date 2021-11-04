package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCareGroupSimpleResponse {

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("activity_time")
    private String activityTime;

    @Builder
    public ChildCareGroupSimpleResponse(final Long groupId,
                                        final String groupName, final String activityTime) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.activityTime = activityTime;
    }

    public static ChildCareGroupSimpleResponse of(final ChildCareGroup group) {
        return ChildCareGroupSimpleResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .activityTime(group.getRegularMeetingDay())
                .build();
    }

}
