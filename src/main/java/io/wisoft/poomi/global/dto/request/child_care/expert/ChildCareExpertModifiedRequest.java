package io.wisoft.poomi.global.dto.request.child_care.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.configures.web.validator.expert.child.ExpertChildId;
import io.wisoft.poomi.configures.web.validator.expert.time.ExpertActivityTime;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
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
public class ChildCareExpertModifiedRequest {

    private String contents;

    @JsonProperty("recruit_type")
    private RecruitType recruitType;

    @JsonProperty("child_id")
    @Min(
            value = 1,
            message = "자식 정보가 부적절합니다."
    )
    private Long childId;

    @JsonProperty("start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;

    public void setRecruitType(final String recruitType) {
        this.recruitType = RecruitType.valueOf(recruitType);
    }

    public RecruitType getRecruitType() {
        return this.recruitType;
    }

}
