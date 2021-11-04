package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberDoingGroupResponse {

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("activity_time")
    private String activityTime;

    @JsonProperty("participation_type")
    private ParticipationType participationType;

    @JsonProperty("participating_groups")
    private List<MemberParticipatingGroupResponse> participatingGroups;

    @JsonProperty("apply_info")
    private List<GroupApplyDetailResponse> applyInfo;

}
