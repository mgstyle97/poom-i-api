package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ChildCareExpertModifiedResponse {

    @JsonProperty("childminder_urgent_id")
    private Long childminderUrgentId;

    private String contents;

    @JsonProperty("recruit_type")
    private RecruitType recruitType;

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
    private ChildCareExpertModifiedResponse(final Long childminderUrgentId,
                                            final String contents,
                                            final RecruitType recruitType,
                                            final LocalDateTime startTime, final LocalDateTime endTime) {
        this.childminderUrgentId = childminderUrgentId;
        this.contents = contents;
        this.recruitType = recruitType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registeredAt = new Date();
    }

    public static ChildCareExpertModifiedResponse of(final ChildCareExpert childCareExpert) {
        return ChildCareExpertModifiedResponse.builder()
                .childminderUrgentId(childCareExpert.getId())
                .contents(childCareExpert.getContents())
                .recruitType(childCareExpert.getRecruitType())
                .startTime(childCareExpert.getStartTime())
                .endTime(childCareExpert.getEndTime())
                .build();
    }

}
