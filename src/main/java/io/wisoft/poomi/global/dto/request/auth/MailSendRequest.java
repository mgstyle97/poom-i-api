package io.wisoft.poomi.global.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MailSendRequest {

    @NotBlank(message = "인증할 이메일을 입력해주세요.")
    private String email;

}
