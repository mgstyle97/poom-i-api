package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildAndPoomiResponse {

    @JsonProperty("child_info")
    private ChildDetailResponse childInfo;

}
