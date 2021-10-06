package io.wisoft.poomi.global.dto.request.child_care.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCareGroupApplyRequest {

    private String contents;

    @JsonProperty("child_id")
    private Long childId;

}
