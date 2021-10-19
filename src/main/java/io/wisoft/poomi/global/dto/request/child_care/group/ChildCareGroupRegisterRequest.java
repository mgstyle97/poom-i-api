package io.wisoft.poomi.global.dto.request.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChildCareGroupRegisterRequest {

    @NotBlank(message = "품앗이반의 이름을 입력해주세요.")
    private String name;

    @JsonProperty("regular_meeting_day")
    @NotBlank(message = "품앗이반의 정기모임일을 입력해주세요.")
    private String regularMeetingDay;

    @JsonProperty("main_activity")
    @NotBlank(message = "품앗이반의 주요 활동을 입력해주세요.")
    private String mainActivity;

    @NotBlank(message = "품앗이반의 소개를 입력해주세요.")
    private String description;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    @JsonProperty("meta_data")
    private String metaData;

    public void setRecruitmentStatus(final String recruitmentStatus) {
        if (StringUtils.hasText(recruitmentStatus)) {
            this.recruitmentStatus = RecruitmentStatus.valueOf(recruitmentStatus);
        } else {
            this.recruitmentStatus = RecruitmentStatus.CLOSED;
        }

    }

}
