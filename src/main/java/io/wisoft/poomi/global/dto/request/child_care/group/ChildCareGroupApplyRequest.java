package io.wisoft.poomi.global.dto.request.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.apply.GroupApply;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChildCareGroupApplyRequest {

    @NotBlank(message = "품앗이반에 지원하기 위한 포부를 적어주세요.")
    private String contents;

    @Min(
            value = 1,
            message = "자식 정보가 부적절합니다."
    )
    @JsonProperty("child_id")
    private Long childId;

}
