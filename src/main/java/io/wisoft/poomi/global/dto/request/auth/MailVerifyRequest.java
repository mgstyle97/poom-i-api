package io.wisoft.poomi.global.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MailVerifyRequest {

    @Email(message = "인증할 이메일 형식이 부적절합니다.")
    @NotBlank(message = "인증할 이메일을 입력해주세요.")
    private String email;

    @Size(
            min = 6, max = 6,
            message = "인증번호 6자리를 입력해주세요."
    )
    @NotBlank(message = "인증번호를 입력해주세요.")
    @JsonProperty("certification_number")
    private String certificationNumber;

}
