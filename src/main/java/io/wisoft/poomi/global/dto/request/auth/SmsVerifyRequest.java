package io.wisoft.poomi.global.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SmsVerifyRequest {

    @JsonProperty("phone_number")
    @NotBlank(message = "인증할 휴대전화 번호가 부적절합니다.")
    private String phoneNumber;

    @JsonProperty("certification_number")
    @NotBlank(message = "전송된 인증번호를 입력해야 합니다.")
    private String certificationNumber;

}
