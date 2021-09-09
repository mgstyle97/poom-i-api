package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class AddressRegisterRequest {

    @JsonProperty("post_code")
    @Size(min = 5, max = 6, message = "우편번호 양식에 맞지 않습니다.")
    private String postCode;

    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @JsonProperty("extra_address")
    private String extraAddress;

}
