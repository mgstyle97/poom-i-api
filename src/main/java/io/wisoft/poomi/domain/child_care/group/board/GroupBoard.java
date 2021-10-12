package io.wisoft.poomi.domain.child_care.group.board;

import io.wisoft.poomi.domain.child_care.BaseTimeEntity;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "group_board_sequence_generator",
        sequenceName = "group_board_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "group_board")
public class GroupBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_board_sequence_generator"
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
            name = "writer_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @OneToMany(mappedBy = "board")
    private Set<Image> images;

    @OneToMany(mappedBy = "board")
    private Set<Comment> comments;

    @Builder
    public GroupBoard(final String contents, final ChildCareGroup childCareGroup,
                      final Member writer) {
        this.contents = contents;
        this.childCareGroup = childCareGroup;
        this.writer = writer;
        this.images = new HashSet<>();
        this.comments = new HashSet<>();
    }

    public static GroupBoard of(final GroupBoardRegisterRequest registerRequest,
                                final ChildCareGroup childCareGroup,
                                final Member writer) {
        return GroupBoard.builder()
                .contents(registerRequest.getContents())
                .childCareGroup(childCareGroup)
                .writer(writer)
                .build();
    }

    public void addImage(final Image image) {
        this.images.add(image);
    }

    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

}
