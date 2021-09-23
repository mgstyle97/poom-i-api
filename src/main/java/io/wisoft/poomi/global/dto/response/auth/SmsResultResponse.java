package io.wisoft.poomi.global.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsResultResponse {

    private String statusCode;
    private String statusName;
    private String requestId;
    private Timestamp timestamp;

}
