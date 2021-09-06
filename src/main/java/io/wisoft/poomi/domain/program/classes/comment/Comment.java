package io.wisoft.poomi.domain.program.classes.comment;

import io.wisoft.poomi.bind.request.CommentRegistRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.program.BaseTimeEntity;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
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
            name = "class_id",
            referencedColumnName = "id"
    )
    private ClassProgram classProgram;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @Builder
    public Comment(final String contents, final ClassProgram classProgram, final Member writer) {
        this.contents = contents;
        this.classProgram = classProgram;
        this.writer = writer;
    }

    public static Comment of(final CommentRegistRequest commentRegistRequest,
                             final Member writer,
                             final ClassProgram classProgram) {
        Comment comment = Comment.builder()
                .contents(commentRegistRequest.getContents())
                .classProgram(classProgram)
                .writer(writer)
                .build();
        classProgram.addComment(comment);

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
