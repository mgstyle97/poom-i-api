package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.global.dto.response.child_care.group.apply.GroupApplyDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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

    @Builder
    public MemberDoingGroupResponse(final Long groupId,
                                    final String groupName, final String activityTime,
                                    final ParticipationType participationType,
                                    final List<MemberParticipatingGroupResponse> participatingGroups,
                                    final List<GroupApplyDetailResponse> applyInfo) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.activityTime = activityTime;
        this.participationType = participationType;
        this.participatingGroups = participatingGroups;
        this.applyInfo = applyInfo;
    }

    public static MemberDoingGroupResponse of(final ChildCareGroup group, final ParticipationType participationType) {
        return MemberDoingGroupResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .activityTime(group.getRegularMeetingDay())
                .participationType(participationType)
                .participatingGroups(generateParticipatingGroups(group))
                .applyInfo(generateApplyInfo(group))
                .build();
    }

    private static List<MemberParticipatingGroupResponse> generateParticipatingGroups(final ChildCareGroup group) {
        return group.getParticipatingMembers().stream()
                .map(MemberParticipatingGroupResponse::of)
                .collect(Collectors.toList());
    }

    private static List<GroupApplyDetailResponse> generateApplyInfo(final ChildCareGroup group) {
        return group.getApplies().stream()
                .map(GroupApplyDetailResponse::of)
                .collect(Collectors.toList());
    }

}
