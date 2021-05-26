package io.wisoft.poomi.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentModifiedDto {

    private String contents;
    private boolean isSecret;

}
