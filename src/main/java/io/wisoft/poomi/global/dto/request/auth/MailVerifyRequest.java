package io.wisoft.poomi.global.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVerifyRequest {

    private String email;

    @JsonProperty("certification_number")
    private String certificationNumber;

}
