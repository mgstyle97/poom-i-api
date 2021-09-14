package io.wisoft.poomi.bind.dto.childminder.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildminderClassDeleteDto {

    private Long id;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    private ChildminderClassDeleteDto(final Long id) {
        this.id = id;
        this.requestedAt = new Date();
    }

    public static ChildminderClassDeleteDto of(final Long id) {
        return new ChildminderClassDeleteDto(id);
    }

}
