package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildCareGroupModifiedResponse {

    private Long id;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    private ChildCareGroupModifiedResponse(final Long id) {
        this.id = id;
        this.requestedAt = new Date();
    }

    public static ChildCareGroupModifiedResponse of(final Long id) {

        return new ChildCareGroupModifiedResponse(id);
    }

}
