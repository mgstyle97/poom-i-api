package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.response.child_care.expert.apply.ExpertApplyApprovedDetailResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.apply.ExpertApplyDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberDoingExpertResponse {

    @JsonProperty("expert_id")
    private Long expertId;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime endTime;

    private String contents;

    @JsonProperty("approve_info")
    private ExpertApplyApprovedDetailResponse approveInfo;

    @JsonProperty("apply_info")
    private List<ExpertApplyDetailResponse> applyInfo;

    @Builder
    public MemberDoingExpertResponse(final Long expertId,
                                     final LocalDateTime startTime, final LocalDateTime endTime,
                                     final String contents,
                                     final ExpertApplyApprovedDetailResponse approveInfo,
                                     final List<ExpertApplyDetailResponse> applyInfo) {
        this.expertId = expertId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.contents = contents;
        this.approveInfo = approveInfo;
        this.applyInfo = applyInfo;
    }

    public static MemberDoingExpertResponse of(final ChildCareExpert expert) {
        final ExpertApplyApprovedDetailResponse approveInfo = generateApproveInfo(expert.getCaringChild());

        return MemberDoingExpertResponse.builder()
                .expertId(expert.getId())
                .startTime(expert.getStartTime())
                .endTime(expert.getEndTime())
                .contents(expert.getContents())
                .approveInfo(approveInfo)
                .applyInfo(
                        expert.getApplications().stream()
                                .map(ExpertApplyDetailResponse::of)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static ExpertApplyApprovedDetailResponse generateApproveInfo(final Child caringChild) {
        if (caringChild != null) {
            return ExpertApplyApprovedDetailResponse.of(caringChild);
        }

        return null;
    }

}
