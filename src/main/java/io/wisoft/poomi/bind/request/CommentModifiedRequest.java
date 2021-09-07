package io.wisoft.poomi.bind.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentModifiedRequest {

    @NotBlank(message = "변경할 댓글 내용을 입력해주세요")
    private String contents;

}
