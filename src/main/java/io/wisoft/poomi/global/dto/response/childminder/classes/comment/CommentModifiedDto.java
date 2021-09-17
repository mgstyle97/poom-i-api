package io.wisoft.poomi.global.dto.response.childminder.classes.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.classes.comment.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentModifiedDto {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public CommentModifiedDto(final Long commentId) {
        this.commentId = commentId;
        this.requestedAt = new Date();
    }

    public static CommentModifiedDto of(final Comment comment) {
        return new CommentModifiedDto(comment.getId());
    }

}
