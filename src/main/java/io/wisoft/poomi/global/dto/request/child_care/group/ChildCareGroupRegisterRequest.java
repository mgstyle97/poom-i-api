package io.wisoft.poomi.global.dto.request.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChildCareGroupRegisterRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    private Long capacity;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;


}
