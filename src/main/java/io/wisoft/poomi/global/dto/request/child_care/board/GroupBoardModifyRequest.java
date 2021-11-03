package io.wisoft.poomi.global.dto.request.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class GroupBoardModifyRequest {

    @JsonProperty("group_id")
    @Min(
            value = 1,
            message = "수정할 품앗이반의 정보가 부적절합니다."
    )
    private Long groupId;

    private String contents;

    @JsonProperty("remove_image_ids")
    private List<Long> removeImageIds;

    @JsonProperty("images")
    private List<String> imageDataList;

}
