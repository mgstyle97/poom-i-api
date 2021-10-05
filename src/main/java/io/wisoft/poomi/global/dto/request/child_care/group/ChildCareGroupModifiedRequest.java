package io.wisoft.poomi.global.dto.request.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class ChildCareGroupModifiedRequest {

    private String contents;

    private Long capacity;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    public void setRecruitmentStatus(final String recruitmentStatus) {
        this.recruitmentStatus = RecruitmentStatus.valueOf(recruitmentStatus);
    }

}
