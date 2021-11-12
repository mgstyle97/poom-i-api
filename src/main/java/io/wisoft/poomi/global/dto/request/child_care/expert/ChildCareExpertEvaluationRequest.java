package io.wisoft.poomi.global.dto.request.child_care.expert;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChildCareExpertEvaluationRequest {

    @Min(value = 0, message = "품앗이 평가 점수는 0점 보다 낮을 수 없습니다.")
    @Max(value = 5, message = "품앗이 평가 점수는 5점 보다 클 수 없습니다.")
    @NotNull(message = "품앗이 평가 점수를 입력해야 합니다.")
    private Integer score;

}
