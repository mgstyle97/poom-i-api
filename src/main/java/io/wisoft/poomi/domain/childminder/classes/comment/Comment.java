package io.wisoft.poomi.domain.childminder.classes.comment;

import io.wisoft.poomi.global.dto.request.childminder.comment.CommentRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.childminder.BaseChildminderEntity;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
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
public class Comment extends BaseChildminderEntity {

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
    private ChildminderClass childminderClass;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @Builder
    public Comment(final String contents, final ChildminderClass childminderClass, final Member writer) {
        this.contents = contents;
        this.childminderClass = childminderClass;
        this.writer = writer;
    }

    public static Comment of(final CommentRegisterRequest commentRegisterRequest,
                             final Member writer,
                             final ChildminderClass childminderClass) {
        Comment comment = Comment.builder()
                .contents(commentRegisterRequest.getContents())
                .childminderClass(childminderClass)
                .writer(writer)
                .build();
        childminderClass.addComment(comment);

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
