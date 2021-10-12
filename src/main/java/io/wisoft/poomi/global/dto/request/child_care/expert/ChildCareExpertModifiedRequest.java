package io.wisoft.poomi.global.dto.request.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChildCareExpertModifiedRequest {

    private String contents;

    @JsonProperty("recruit_type")
    private RecruitType recruitType;

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;

    public void setRecruitType(final String recruitType) {
        this.recruitType = RecruitType.valueOf(recruitType);
    }

    public RecruitType getRecruitType() {
        return this.recruitType;
    }

}
