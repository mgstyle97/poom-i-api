package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildCareExpertLookupResponse(final ChildCareExpert childCareExpert) {
        this.expertId = childCareExpert.getId();
        this.writer = childCareExpert.getWriter().getNick();
        this.childId = childCareExpert.getCaringChild().getId();
        this.writerScore = childCareExpert.getWriter().getScore();
        this.likedCount = childCareExpert.getLikes().size();
        this.appliedCount = childCareExpert.getApplications().size();
        this.contents = childCareExpert.getContents();
        this.recruitType = childCareExpert.getRecruitType();
        this.createdAt = Timestamp.valueOf(childCareExpert.getCreatedAt());
        this.startDate = LocalDateTimeUtils.getDateToString(childCareExpert.getStartTime());
        this.startTime = LocalDateTimeUtils.getTimeToString(childCareExpert.getStartTime());
        this.endDate = LocalDateTimeUtils.getDateToString(childCareExpert.getEndTime());
        this.endTime = LocalDateTimeUtils.getTimeToString(childCareExpert.getEndTime());
        this.registeredAt = new Date();
    }

    public static ChildCareExpertLookupResponse of(final ChildCareExpert childCareExpert) {
        return ChildCareExpertLookupResponse.builder()
                .childCareExpert(childCareExpert)
                .build();
    }

}
