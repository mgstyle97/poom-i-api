package io.wisoft.poomi.global.dto.request.child_care.playground;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResidenceCertificationRegisterRequest {

    @NotBlank(message = "거주지 인증을 위한 파일을 첨부해야 합니다.")
    @JsonProperty("file")
    private String fileData;

}
