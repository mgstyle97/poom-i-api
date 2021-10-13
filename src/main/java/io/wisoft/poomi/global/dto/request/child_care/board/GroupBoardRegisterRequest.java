package io.wisoft.poomi.global.dto.request.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GroupBoardRegisterRequest {

    @Min(1)
    @JsonProperty("group_id")
    private Long groupId;

    @NotBlank(message = "게시글의 내용을 입력해주세요.")
    private String contents;

}
