package io.wisoft.poomi.domain.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.wisoft.poomi.domain.board.Board;
import io.wisoft.poomi.domain.comment.dto.CommentPostDto;
import io.wisoft.poomi.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@SequenceGenerator(
        name = "comment_sequence_generator",
        sequenceName = "comment_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Comment {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence_generator"
    )
    @Column(name = "comment_id")
    private Long id;

    private String contents;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_secret")
    private boolean isSecret;

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    public static Comment of(final Member member, final Board board,
                      final CommentPostDto commentPostDto) {
        return Comment.builder()
                .contents(commentPostDto.getContents())
                .createdAt(LocalDateTime.now())
                .isSecret(commentPostDto.isSecret())
                .board(board)
                .member(member)
                .build();
    }

    public Comment changeContents(final String contents) {
        if ((contents != null) &&
                (!this.contents.equals(contents))) {
            this.contents = contents;
        }

        return this;
    }

    public Comment changeIsSecret(final boolean isSecret) {
        if (this.isSecret != isSecret) {
            this.isSecret = isSecret;
        }

        return this;
    }
}
