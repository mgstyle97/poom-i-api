package io.wisoft.poomi.global.dto.response.child_care.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentRegistResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("board_id")
    private Long boardId;

    @JsonProperty("writer_id")
    private Long writerId;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public CommentRegistResponse(final Comment comment) {
        this.commentId = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.writerId = comment.getWriter().getId();
        this.requestedAt = new Date();
    }

    public static CommentRegistResponse of(final Comment comment) {
        return new CommentRegistResponse(comment);
    }

}
