package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClassProgramDeleteDto {

    private Long id;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public ClassProgramDeleteDto(final Long id) {
        this.id = id;
        this.requestedAt = new Date();
    }

    public static ClassProgramDeleteDto of(final Long id) {
        return new ClassProgramDeleteDto(id);
    }

}
