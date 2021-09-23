package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CMInfoRegisterResponse {


    private Long id;

    private String email;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime registeredAt;

    public CMInfoRegisterResponse(final Long id, final String email) {
        this.id = id;
        this.email = email;
        this.registeredAt = LocalDateTime.now();
    }

}
