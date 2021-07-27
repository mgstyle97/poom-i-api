package io.wisoft.poomi.bind.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CMInfoRegisterDto {

    private Long cmInfoId;
    private String userId;
    private Date registeredAt;

}
