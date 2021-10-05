package io.wisoft.poomi.global.dto.response.child_care.group.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDeleteResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public CommentDeleteResponse(final Long commentId) {
        this.commentId = commentId;
        this.requestedAt = new Date();
    }

    public static CommentDeleteResponse of(final Comment comment) {
        return new CommentDeleteResponse(comment.getId());
    }

}
