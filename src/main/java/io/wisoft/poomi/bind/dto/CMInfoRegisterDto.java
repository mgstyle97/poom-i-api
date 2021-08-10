package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class CMInfoRegisterDto {


    private Long id;

    private String email;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime registeredAt;

    public CMInfoRegisterDto(final Long id, final String email) {
        this.id = id;
        this.email = email;
        this.registeredAt = LocalDateTime.now();
    }

}
