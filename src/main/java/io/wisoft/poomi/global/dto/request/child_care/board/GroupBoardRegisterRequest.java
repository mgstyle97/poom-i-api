package io.wisoft.poomi.global.dto.request.child_care.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.configures.web.validator.image.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
public class GroupBoardRegisterRequest {

    @Min(1)
    @JsonProperty("group_id")
    private Long groupId;

    @NotBlank(message = "게시글의 내용을 입력해주세요.")
    private String contents;

    @JsonProperty("images")
    private List<String> images;

}
