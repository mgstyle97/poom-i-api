package io.wisoft.poomi.domain.child_care.group.board;

import io.wisoft.poomi.domain.child_care.BaseTimeEntity;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.child_care.group.board.image.BoardImage;
import io.wisoft.poomi.domain.image.Image;
import io.wisoft.poomi.domain.member.Member;
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
    private Set<BoardImage> images;

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

    public Set<BoardImage> getBoardImages() {
        return this.images;
    }

    public Set<Image> getImages() {
        return this.images.stream()
                .map(BoardImage::getImage)
                .collect(Collectors.toSet());
    }

    public List<String> getImageURIs() {
        return this.images.stream()
                .map(BoardImage::getImage)
                .map(Image::getImageURI)
                .collect(Collectors.toList());
    }

    public BoardImage addImage(final Image image) {
        final BoardImage boardImage = BoardImage.builder()
                .board(this)
                .image(image)
                .build();
        this.images.add(
                boardImage
        );

        return boardImage;
    }

    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

    public void changeGroup(final ChildCareGroup childCareGroup) {
        if (!this.childCareGroup.equals(childCareGroup)) {
            this.childCareGroup.removeBoard(this);
            this.childCareGroup = childCareGroup;
            childCareGroup.addBoard(this);
        }
    }

    public void changeContents(final String changeContents) {
        if (StringUtils.hasText(changeContents) && !this.contents.equals(changeContents)) {
            this.contents = changeContents;
        }
    }

    public void removeImage(final Image image) {
        this.images.remove(image);
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
