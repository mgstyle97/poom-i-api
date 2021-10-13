package io.wisoft.poomi.domain.child_care.group.board;

import io.wisoft.poomi.domain.child_care.BaseTimeEntity;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardModifyRequest;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "board_likes",
            joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")}
    )
    private Set<Member> likes;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public GroupBoard(final String contents, final ChildCareGroup childCareGroup,
                      final Member writer) {
        this.contents = contents;
        this.childCareGroup = childCareGroup;
        this.writer = writer;
        this.images = new HashSet<>();
        this.comments = new HashSet<>();
        this.likes = new HashSet<>();
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

    public List<String> getImageURIs() {
        return this.images.stream()
                .map(Image::getImageUri)
                .collect(Collectors.toList());
    }

    public void addImage(final Image image) {
        this.images.add(image);
    }

    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

    public void modifiedFor(final GroupBoardModifyRequest modifyRequest) {
        final String changeContents = modifyRequest.getContents();
        if (StringUtils.hasText(changeContents) && !this.contents.equals(changeContents)) {
            this.contents = changeContents;
        }
    }

    public void resetAssociated() {
        this.likes.forEach(member -> member.removeLikeBoard(this));
        this.likes.clear();

    }

    public void addLikeMember(final Member member) {
        this.likes.add(member);
        member.addLikeBoard(this);
    }

    public void removeLikeMember(final Member member) {
        this.likes.remove(member);
        member.removeLikeBoard(this);
    }

}
