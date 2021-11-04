package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Getter
@Setter
public class ChildCareExpertLookupResponse {

    @JsonProperty("expert_id")
    private Long expertId;

    private String writer;

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("writer_score")
    private Integer writerScore;

    @JsonProperty("liked_count")
    private Integer likedCount;

    @JsonProperty("applied_count")
    private Integer appliedCount;

    private String contents;

    @JsonProperty("recruit_type")
    private RecruitType recruitType;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("apply_status")
    private ApplyStatus applyStatus;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    @Builder
    private ChildCareExpertLookupResponse(final ChildCareExpert childCareExpert,
                                          final Member member) {
        this.expertId = childCareExpert.getId();
        this.writer = childCareExpert.getWriter().getNick();
        Optional.ofNullable(childCareExpert.getCaringChild()).ifPresent(child -> {
            this.childId = child.getId();
        });
        this.writerScore = childCareExpert.getWriter().getScore();
        this.likedCount = childCareExpert.getLikes().size();
        this.appliedCount = childCareExpert.getApplications().size();
        this.contents = childCareExpert.getContents();
        this.recruitType = childCareExpert.getRecruitType();
        this.createdAt = LocalDateTimeUtils.getDateToString(childCareExpert.getCreatedAt());
        this.startDate = LocalDateTimeUtils.getDateToString(childCareExpert.getStartTime());
        this.startTime = LocalDateTimeUtils.getTimeToString(childCareExpert.getStartTime());
        this.endDate = LocalDateTimeUtils.getDateToString(childCareExpert.getEndTime());
        this.endTime = LocalDateTimeUtils.getTimeToString(childCareExpert.getEndTime());
        this.applyStatus = getApplyStatus(childCareExpert, member);
        this.requestedAt = new Date();
    }

    public static ChildCareExpertLookupResponse of(final ChildCareExpert childCareExpert,
                                                   final Member member) {
        return ChildCareExpertLookupResponse.builder()
                .childCareExpert(childCareExpert)
                .member(member)
                .build();
    }

    private ApplyStatus getApplyStatus(final ChildCareExpert expert,
                                       final Member member) {
        Optional<Member> optionalMember = expert.getApplications().stream()
                .map(ChildCareExpertApply::getWriter)
                .filter(applier -> applier.equals(member))
                .findFirst();
        if (optionalMember.isPresent()) {
            return ApplyStatus.APPLY;
        }

        return ApplyStatus.NOT_APPLY;
    }

    private enum ApplyStatus {
        APPLY, NOT_APPLY
    }

}
