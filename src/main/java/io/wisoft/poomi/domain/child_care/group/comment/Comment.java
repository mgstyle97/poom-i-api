package io.wisoft.poomi.domain.child_care.group.comment;

import io.wisoft.poomi.domain.child_care.BaseTimeEntity;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.global.dto.request.child_care.group.comment.CommentRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.child_care.BaseChildCareEntity;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "comment_sequence_generator",
        sequenceName = "comment_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(
            name = "contents",
            columnDefinition = "TEXT"
    )
    private String contents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "board_id",
            referencedColumnName = "id"
    )
    private GroupBoard board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "writer_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @Builder
    public Comment(final String contents, final GroupBoard board, final Member writer) {
        this.contents = contents;
        this.board = board;
        this.writer = writer;
    }

    public static Comment of(final CommentRegisterRequest commentRegisterRequest,
                             final Member writer,
                             final GroupBoard board) {
        Comment comment = Comment.builder()
                .contents(commentRegisterRequest.getContents())
                .board(board)
                .writer(writer)
                .build();
        board.addComment(comment);

        return comment;
    }

    public void updateContents(final String contents) {
        if (!StringUtils.hasText(contents) &&
                this.contents.equals(contents)) {
            return;
        }
        this.contents = contents;
    }

}
