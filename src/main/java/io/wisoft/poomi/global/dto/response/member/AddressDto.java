package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddressDto {

    private String email;

    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    public static AddressDto from(String email, String tagName) {
        AddressDto addressDto = new AddressDto();
        addressDto.setEmail(email);
        addressDto.setTagName(tagName);
        addressDto.setRegisteredAt(new Date());

        return addressDto;
    }

}
