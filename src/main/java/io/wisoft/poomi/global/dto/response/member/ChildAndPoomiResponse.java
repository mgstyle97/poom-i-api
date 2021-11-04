package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.response.child_care.expert.MemberDoingExpertResponse;
import io.wisoft.poomi.global.dto.response.child_care.group.MemberDoingGroupResponse;
import io.wisoft.poomi.global.dto.response.member.child.ChildDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChildAndPoomiResponse {

    @JsonProperty("child_info")
    private List<ChildDetailResponse> childInfo;

    @JsonProperty("expert_info")
    private List<MemberDoingExpertResponse> expertInfo;

    @JsonProperty("group_info")
    private List<MemberDoingGroupResponse> groupInfo;

    @Builder
    public ChildAndPoomiResponse(final List<ChildDetailResponse> childInfo,
                                 final List<MemberDoingExpertResponse> expertInfo,
                                 final List<MemberDoingGroupResponse> groupInfo) {
        this.childInfo = childInfo;
        this.expertInfo = expertInfo;
        this.groupInfo = groupInfo;
    }

    public static ChildAndPoomiResponse of(final Member member) {
        return ChildAndPoomiResponse.builder()
                .childInfo(
                        member.getChildren().stream()
                                .map(ChildDetailResponse::of)
                                .collect(Collectors.toList())
                )
                .expertInfo(
                        member.getWrittenExpertContents().stream()
                                .map(MemberDoingExpertResponse::of)
                                .collect(Collectors.toList())
                )
                .groupInfo(
                        member.getChildCareGroupProperties().getParticipatingGroups().stream()
                                .map(groupParticipatingMember -> MemberDoingGroupResponse.of(
                                        groupParticipatingMember.getGroup(),
                                        groupParticipatingMember.getParticipationType()
                                ))
                                .collect(Collectors.toList())
                )
                .build();
    }

}
