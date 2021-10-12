package io.wisoft.poomi.global.dto.request.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupBoardRegisterRequest {

    @JsonProperty("group_id")
    private Long groupId;

    private String contents;

}
