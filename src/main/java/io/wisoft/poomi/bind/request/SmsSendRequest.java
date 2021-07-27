package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSendRequest {

    @JsonProperty("phone_number")
    private String phoneNumber;

}
