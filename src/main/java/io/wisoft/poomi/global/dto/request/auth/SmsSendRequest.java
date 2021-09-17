package io.wisoft.poomi.global.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SmsSendRequest {

    @JsonProperty("phone_number")
    @NotBlank(message = "전화번호가 없거나 빈칸이면 안됩니다.")
    private String phoneNumber;

}
