package io.wisoft.poomi.domain.childminder.classes;

import io.wisoft.poomi.global.dto.request.childminder.classes.ChildminderClassModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.classes.ChildminderClassRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.childminder.BaseChildminderEntity;
import io.wisoft.poomi.domain.childminder.classes.comment.Comment;
import io.wisoft.poomi.domain.childminder.classes.image.Image;
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
        name = "class_sequence_generator",
        sequenceName = "class_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "childminder_class")
public class ChildminderClass extends BaseChildminderEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "class_sequence_generator"
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

    @OneToMany(mappedBy = "childminderClass")
    private Set<Image> images;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "class_applier",
            joinColumns = {@JoinColumn(name = "class_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")}
    )
    private Set<Member> appliers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "class_likes",
        joinColumns = {@JoinColumn(name = "class_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")}
    )
    private Set<Member> likes;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "childminderClass"
    )
    private Set<Comment> comments;

    @Builder
    public ChildminderClass(final String title, final String contents,
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

    public static ChildminderClass of(final Member member,
                                      final ChildminderClassRegisterRequest childminderClassRegisterRequest) {
        ChildminderClass childminderClass = ChildminderClass.builder()
                .title(childminderClassRegisterRequest.getTitle())
                .contents(childminderClassRegisterRequest.getContents())
                .capacity(childminderClassRegisterRequest.getCapacity())
                .isRecruit(childminderClassRegisterRequest.getIsRecruit())
                .writer(member)
                .build();
        member.addClass(childminderClass);

        return childminderClass;
    }

    public void setImages(final Set<Image> images) {
        this.images = images;
    }

    public void addApplier(final Member member) {
        if (!this.appliers.contains(member)) {
            this.appliers.add(member);
            member.addAppliedClass(this);
        }
    }

    public void addLikes(final Member member) {
        if (!this.likes.contains(member)) {
            this.likes.add(member);
            member.addLikedClass(this);
        }
    }

    public void modifiedFor(final ChildminderClassModifiedRequest childminderClassModifiedRequest) {
        changeContents(childminderClassModifiedRequest.getContents());
        changeIsRecruit(childminderClassModifiedRequest.getIsRecruit());
        changeCapacity(childminderClassModifiedRequest.getCapacity());
    }

    public void resetAssociated() {
        getWriter().removeWrittenClass(this);
        this.appliers.forEach(applier -> applier.removeAppliedClass(this));
        this.likes.forEach(like -> like.removeLikedClass(this));
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
