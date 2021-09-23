package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddressResponse {

    private String email;

    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    public static AddressResponse from(String email, String tagName) {
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setEmail(email);
        addressResponse.setTagName(tagName);
        addressResponse.setRegisteredAt(new Date());

        return addressResponse;
    }

}
