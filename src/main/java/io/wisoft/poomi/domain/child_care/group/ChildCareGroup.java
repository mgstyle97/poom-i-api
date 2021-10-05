package io.wisoft.poomi.domain.child_care.group;

import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.child_care.BaseChildCareEntity;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "group_sequence_generator",
        sequenceName = "group_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "child_care_group")
public class ChildCareGroup extends BaseChildCareEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(
            name = "contents",
            columnDefinition = "TEXT"
    )
    private String contents;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "is_recruit")
    private Boolean isRecruit;

    @OneToMany(mappedBy = "childCareGroup")
    private Set<Image> images;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_apply",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")}
    )
    private Set<Member> appliers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_likes",
        joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")}
    )
    private Set<Member> likes;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "childCareGroup"
    )
    private Set<Comment> comments;

    @Builder
    public ChildCareGroup(final String title, final String contents,
                          final Long capacity,
                          final Boolean isRecruit, final Member writer) {
        this.title = title;
        this.contents = contents;
        this.capacity = capacity;
        this.isRecruit = isRecruit;
        this.images = new HashSet<>();
        this.appliers = new HashSet<>();
        this.likes = new HashSet<>();
        this.comments = new HashSet<>();
        setWriter(writer);
        setAddressTag(writer.getAddressTag());
    }

    public static ChildCareGroup of(final Member member,
                                    final ChildCareGroupRegisterRequest childCareGroupRegisterRequest) {
        ChildCareGroup childCareGroup = ChildCareGroup.builder()
                .title(childCareGroupRegisterRequest.getTitle())
                .contents(childCareGroupRegisterRequest.getContents())
                .capacity(childCareGroupRegisterRequest.getCapacity())
                .isRecruit(childCareGroupRegisterRequest.getIsRecruit())
                .writer(member)
                .build();
        member.addGroup(childCareGroup);

        return childCareGroup;
    }

    public void setImages(final Set<Image> images) {
        this.images = images;
    }

    public void addApplier(final Member member) {
        if (!this.appliers.contains(member)) {
            this.appliers.add(member);
            member.addAppliedGroup(this);
        }
    }

    public void addLikes(final Member member) {
        if (!this.likes.contains(member)) {
            this.likes.add(member);
            member.addLikedGroup(this);
        }
    }

    public void modifiedFor(final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        changeContents(childCareGroupModifiedRequest.getContents());
        changeIsRecruit(childCareGroupModifiedRequest.getIsRecruit());
        changeCapacity(childCareGroupModifiedRequest.getCapacity());
    }

    public void resetAssociated() {
        getWriter().removeWrittenGroup(this);
        this.appliers.forEach(applier -> applier.removeAppliedGroup(this));
        this.likes.forEach(like -> like.removeLikedGroup(this));
        this.comments.clear();
    }

    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

    private void changeContents(final String newContents) {
        if (!StringUtils.hasText(newContents) ||
                this.contents.equals(newContents)) {
            return;
        }
        this.contents = newContents;
    }

    private void changeIsRecruit(final Boolean isRecruit) {
        if (isRecruit == null) {
            return;
        }
        this.isRecruit = isRecruit;
    }

    private void changeCapacity(final Long capacity) {
        if (capacity == null) {
            return;
        }
        this.capacity = capacity;
    }

}
