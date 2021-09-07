package io.wisoft.poomi.bind.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentRegistRequest {

    @NotBlank(message = "빈 댓글은 입력할 수 없습니다.")
    private String contents;

}
