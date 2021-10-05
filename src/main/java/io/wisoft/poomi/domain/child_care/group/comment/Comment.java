package io.wisoft.poomi.domain.child_care.group.comment;

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
public class Comment extends BaseChildCareEntity {

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
            name = "group_id",
            referencedColumnName = "id"
    )
    private ChildCareGroup childCareGroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @Builder
    public Comment(final String contents, final ChildCareGroup childCareGroup, final Member writer) {
        this.contents = contents;
        this.childCareGroup = childCareGroup;
        this.writer = writer;
    }

    public static Comment of(final CommentRegisterRequest commentRegisterRequest,
                             final Member writer,
                             final ChildCareGroup childCareGroup) {
        Comment comment = Comment.builder()
                .contents(commentRegisterRequest.getContents())
                .childCareGroup(childCareGroup)
                .writer(writer)
                .build();
        childCareGroup.addComment(comment);

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
