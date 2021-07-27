package io.wisoft.poomi.bind.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsResultDto {

    private String statusCode;
    private String statusName;
    private String requestId;
    private Timestamp timestamp;

}
