package io.wisoft.poomi.global.dto.request.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.configures.web.validator.image.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class GroupBoardRegisterRequest {

    @Min(
            value = 1,
            message = "게시글과 연관된 품앗이반의 정보가 부적절합니다."
    )
    @NotNull(message = "품앗이반의 정보를 입력해주세요.")
    @JsonProperty("group_id")
    private Long groupId;

    @NotBlank(message = "게시글의 내용을 입력해주세요.")
    private String contents;

    @JsonProperty("images")
    private String image;

}
