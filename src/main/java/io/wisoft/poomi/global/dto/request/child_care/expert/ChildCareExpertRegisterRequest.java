package io.wisoft.poomi.global.dto.request.child_care.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChildCareExpertRegisterRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @JsonProperty("recruit_type")
    private RecruitType recruitType;

    @JsonProperty("child_id")
    private Long childId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("end_time")
    private LocalDateTime endTime;

    public void setRecruitType(final String recruitType) {
        try {
            this.recruitType = RecruitType.valueOf(recruitType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("모집 타입이 잘못되었습니다.");
        }
    }

    public RecruitType getRecruitType() {
        return this.recruitType;
    }

}