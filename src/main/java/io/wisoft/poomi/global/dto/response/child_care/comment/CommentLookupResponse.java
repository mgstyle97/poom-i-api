package io.wisoft.poomi.global.dto.response.child_care.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLookupResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    private String writer;

    @JsonProperty("writer_score")
    private Integer writerScore;

    private String contents;

    public CommentLookupResponse(final Comment comment) {
        this.commentId = comment.getId();
        this.writer = comment.getWriter().getNick();
        this.writerScore = comment.getWriter().getScore();
        this.contents = comment.getContents();
    }

    public static CommentLookupResponse of(final Comment comment) {
        return new CommentLookupResponse(comment);
    }

}
