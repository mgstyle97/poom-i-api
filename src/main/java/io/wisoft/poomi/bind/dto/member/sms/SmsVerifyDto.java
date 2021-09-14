package io.wisoft.poomi.bind.dto.member.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsVerifyDto {

    @JsonProperty("phone_number")
    private String phoneNumber;

}
