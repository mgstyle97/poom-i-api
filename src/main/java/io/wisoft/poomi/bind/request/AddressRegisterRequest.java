package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRegisterRequest {

    @JsonProperty("post_code")
    private String postCode;

    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @JsonProperty("extra_address")
    private String extraAddress;

}
