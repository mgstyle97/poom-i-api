package io.wisoft.poomi.global.dto.request.child_care.expert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChildCareExpertApplyRequest {

    @NotBlank(message = "품앗이꾼에 지원하기 위한 메시지를 작성해야 합니다.")
    private String contents;

    @JsonProperty("child_id")
    private Long childId;

}
