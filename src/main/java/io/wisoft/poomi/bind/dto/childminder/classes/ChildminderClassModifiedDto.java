package io.wisoft.poomi.bind.dto.childminder.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildminderClassModifiedDto {

    private Long id;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    private ChildminderClassModifiedDto(final Long id) {
        this.id = id;
        this.requestedAt = new Date();
    }

    public static ChildminderClassModifiedDto of(final Long id) {

        return new ChildminderClassModifiedDto(id);
    }

}
