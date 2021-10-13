package io.wisoft.poomi.global.dto.request.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class ChildCareGroupModifiedRequest {

    private String name;

    @JsonProperty("regular_meeting_day")
    private String regularMeetingDay;

    @JsonProperty("main_activity")
    private String mainActivity;

    private String description;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    public void setRecruitmentStatus(final String recruitmentStatus) {
        if (StringUtils.hasText(recruitmentStatus)) {
            this.recruitmentStatus = RecruitmentStatus.valueOf(recruitmentStatus);
        }
    }

}
