package io.wisoft.poomi.bind.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClassProgramModifiedDto {

    private Long id;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public ClassProgramModifiedDto(final Long id) {
        this.id = id;
        this.requestedAt = new Date();
    }

    public static ClassProgramModifiedDto of(final Long id) {

        return new ClassProgramModifiedDto(id);
    }

}
