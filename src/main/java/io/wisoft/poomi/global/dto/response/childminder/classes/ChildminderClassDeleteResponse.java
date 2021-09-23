package io.wisoft.poomi.global.dto.response.childminder.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildminderClassDeleteResponse {

    private Long id;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    private ChildminderClassDeleteResponse(final Long id) {
        this.id = id;
        this.requestedAt = new Date();
    }

    public static ChildminderClassDeleteResponse of(final Long id) {
        return new ChildminderClassDeleteResponse(id);
    }

}
