package io.wisoft.poomi.global.dto.request.child_care.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.configures.web.validator.expert.child.ExpertChildId;
import io.wisoft.poomi.configures.web.validator.expert.time.ExpertActivityTime;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ExpertChildId(
        recruitType = "recruitType",
        childId = "childId",
        message = "품앗이꾼에게 도움을 요청할 때는 자식 정보가 필수입니다."
)
@ExpertActivityTime(
        startTime = "startTime",
        endTime = "endTime",
        message = "품앗이꾼 활동 시간이 올바르지 않습니다."
)
public class ChildCareExpertRegisterRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @JsonProperty("recruit_type")
    @NotNull(message = "품앗이꾼의 모집 형태를 선택해주세요.")
    private RecruitType recruitType;

    @JsonProperty("child_id")
    private Long childId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("start_time")
    @NotNull(message = "품앗이꾼의 활동 시작 시간을 선택해주세요.")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("end_time")
    @NotNull(message = "품앗이꾼의 활동 마침 예상 시간을 선택해주세요.")
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