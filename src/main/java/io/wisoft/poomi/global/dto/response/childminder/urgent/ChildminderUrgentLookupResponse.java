package io.wisoft.poomi.global.dto.response.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ChildminderUrgentLookupResponse {

    @JsonProperty("childminder_urgent_id")
    private Long childminderUrgentId;

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
    private ChildminderUrgentLookupResponse(final ChildminderUrgent childminderUrgent) {
        this.childminderUrgentId = childminderUrgent.getId();
        this.writer = childminderUrgent.getWriter().getNick();
        this.childminderScore = childminderUrgent.getWriter().getScore();
        this.likedCount = childminderUrgent.getLikes().size();
        this.appliedCount = childminderUrgent.getApplications().size();
        this.contents = childminderUrgent.getContents();
        this.createdAt = Timestamp.valueOf(childminderUrgent.getCreatedAt());
        this.startTime = childminderUrgent.getStartTime();
        this.endTime = childminderUrgent.getEndTime();
        this.registeredAt = new Date();
    }

    public static ChildminderUrgentLookupResponse of(final ChildminderUrgent childminderUrgent) {
        return ChildminderUrgentLookupResponse.builder()
                .childminderUrgent(childminderUrgent)
                .build();
    }

}
