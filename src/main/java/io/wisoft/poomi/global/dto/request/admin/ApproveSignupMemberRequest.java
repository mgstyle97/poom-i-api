package io.wisoft.poomi.global.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ApproveSignupMemberRequest {

    @NotBlank(message = "사용자의 계정을 입력해야 합니다.")
    private String account;

}
