package io.wisoft.poomi.global.dto.response.admin.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalNeedMemberResponse {

    @JsonProperty("member_info")
    private MemberInfoResponse memberInfo;

    @JsonProperty("residence_info")
    private ResidenceInfoResponse residenceInfo;

    @Builder
    public ApprovalNeedMemberResponse(final MemberInfoResponse memberInfo,
                                      final ResidenceInfoResponse residenceInfo) {
        this.memberInfo = memberInfo;
        this.residenceInfo = residenceInfo;
    }

    public static ApprovalNeedMemberResponse of(final Member member) {
        return ApprovalNeedMemberResponse.builder()
                .memberInfo(MemberInfoResponse.of(member))
                .residenceInfo(ResidenceInfoResponse.of(member.getAddress(), member.getResidenceCertification()))
                .build();
    }

}
