package io.wisoft.poomi.global.dto.request.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupBoardModifyRequest {

    @JsonProperty("group_id")
    private Long groupId;

    private String contents;

    @JsonProperty("remove_image_ids")
    private List<Long> removeImageIds;

}
