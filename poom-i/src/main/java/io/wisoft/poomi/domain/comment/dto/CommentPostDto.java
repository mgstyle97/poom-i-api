package io.wisoft.poomi.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPostDto {

    private String contents;
    private boolean isSecret;

}
