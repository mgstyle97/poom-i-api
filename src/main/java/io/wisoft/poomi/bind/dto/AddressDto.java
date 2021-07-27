package io.wisoft.poomi.bind.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddressDto {

    private String loginId;
    private String tagName;
    private Date registeredAt;

    public static AddressDto from(String loginId, String tagName) {
        AddressDto addressDto = new AddressDto();
        addressDto.setLoginId(loginId);
        addressDto.setTagName(tagName);
        addressDto.setRegisteredAt(new Date());

        return addressDto;
    }

}
