package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.global.dto.response.member.child.ChildDetailResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildAndPoomiResponse {

    @JsonProperty("child_info")
    private ChildDetailResponse childInfo;

}
