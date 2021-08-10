package io.wisoft.poomi.bind.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddressDto {

    private String email;
    private String tagName;
    private Date registeredAt;

    public static AddressDto from(String email, String tagName) {
        AddressDto addressDto = new AddressDto();
        addressDto.setEmail(email);
        addressDto.setTagName(tagName);
        addressDto.setRegisteredAt(new Date());

        return addressDto;
    }

}
