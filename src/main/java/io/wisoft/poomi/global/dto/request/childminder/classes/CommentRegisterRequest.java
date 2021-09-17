package io.wisoft.poomi.global.dto.request.childminder.classes;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentRegisterRequest {

    @NotBlank(message = "빈 댓글은 입력할 수 없습니다.")
    private String contents;

}
