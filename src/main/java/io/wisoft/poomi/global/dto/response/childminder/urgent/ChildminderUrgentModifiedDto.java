package io.wisoft.poomi.global.dto.response.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ChildminderUrgentModifiedDto {

    @JsonProperty("childminder_urgent_id")
    private Long childminderUrgentId;

    private String contents;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildminderUrgentModifiedDto(final Long childminderUrgentId,
                                         final String contents,
                                          final Boolean isRecruit,
                                          final LocalDateTime startTime, final LocalDateTime endTime) {
        this.childminderUrgentId = childminderUrgentId;
        this.contents = contents;
        this.isRecruit = isRecruit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registeredAt = new Date();
    }

    public static ChildminderUrgentModifiedDto of(final ChildminderUrgent childminderUrgent) {
        return ChildminderUrgentModifiedDto.builder()
                .childminderUrgentId(childminderUrgent.getId())
                .contents(childminderUrgent.getContents())
                .isRecruit(childminderUrgent.getIsRecruit())
                .startTime(childminderUrgent.getStartTime())
                .endTime(childminderUrgent.getEndTime())
                .build();
    }

}
