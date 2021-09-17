package io.wisoft.poomi.global.dto.response.childminder.classes.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.classes.comment.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentRegistDto {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("class_program_id")
    private Long classProgramId;

    @JsonProperty("writer_id")
    private Long writerId;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public CommentRegistDto(final Comment comment) {
        this.commentId = comment.getId();
        this.classProgramId = comment.getChildminderClass().getId();
        this.writerId = comment.getWriter().getId();
        this.requestedAt = new Date();
    }

    public static CommentRegistDto of(final Comment comment) {
        return new CommentRegistDto(comment);
    }

}
