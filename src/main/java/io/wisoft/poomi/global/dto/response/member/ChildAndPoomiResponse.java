package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.global.dto.response.child_care.expert.MemberDoingExpertResponse;
import io.wisoft.poomi.global.dto.response.child_care.group.MemberDoingGroupResponse;
import io.wisoft.poomi.global.dto.response.member.child.ChildDetailResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChildAndPoomiResponse {

    @JsonProperty("child_info")
    private ChildDetailResponse childInfo;

    @JsonProperty("expert_info")
    private MemberDoingExpertResponse expertInfo;

    @JsonProperty("group_info")
    private List<MemberDoingGroupResponse> groupInfo;

}
