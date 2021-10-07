package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ChildCareExpertLookupResponse {

    @JsonProperty("expert_id")
    private Long expertId;

    private String writer;

    @JsonProperty("childminder_score")
    private Integer childminderScore;

    @JsonProperty("liked_count")
    private Integer likedCount;

    @JsonProperty("applied_count")
    private Integer appliedCount;

    private String contents;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildCareExpertLookupResponse(final ChildCareExpert childCareExpert) {
        this.expertId = childCareExpert.getId();
        this.writer = childCareExpert.getWriter().getNick();
        this.childminderScore = childCareExpert.getWriter().getScore();
        this.likedCount = childCareExpert.getLikes().size();
        this.appliedCount = childCareExpert.getApplications().size();
        this.contents = childCareExpert.getContents();
        this.createdAt = Timestamp.valueOf(childCareExpert.getCreatedAt());
        this.startTime = childCareExpert.getStartTime();
        this.endTime = childCareExpert.getEndTime();
        this.registeredAt = new Date();
    }

    public static ChildCareExpertLookupResponse of(final ChildCareExpert childCareExpert) {
        return ChildCareExpertLookupResponse.builder()
                .childCareExpert(childCareExpert)
                .build();
    }

}
