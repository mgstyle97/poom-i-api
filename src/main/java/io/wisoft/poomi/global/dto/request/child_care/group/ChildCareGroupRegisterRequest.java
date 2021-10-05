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

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    private Long capacity;

    @JsonProperty("recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    public void setRecruitmentStatus(final String recruitmentStatus) {
        if (!StringUtils.hasText(recruitmentStatus)) {
            this.recruitmentStatus = RecruitmentStatus.CLOSED;
        }

        this.recruitmentStatus = RecruitmentStatus.valueOf(recruitmentStatus);
    }

}
