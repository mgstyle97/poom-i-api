package io.wisoft.poomi.domain.program.classes;

import io.wisoft.poomi.bind.request.ClassProgramModifiedRequest;
import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.common.error.exceptions.NoPermissionOfClassProgram;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.program.BaseTimeEntity;
import io.wisoft.poomi.domain.program.classes.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
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
public class ClassProgram extends BaseTimeEntity {

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

    @Column(name = "is_board")
    private Boolean isBoard;

    @Column(name = "expired_at")
    @Temporal(TemporalType.DATE)
    private Date expiredAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_tag_id",
            referencedColumnName = "id"
    )
    private AddressTag addressTag;

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
            mappedBy = "classProgram"
    )
    private Set<Comment> comments;

    @Builder
    public ClassProgram(final String title, final String contents,
                        final Long capacity,
                        final Boolean isRecruit, final Boolean isBoard,
                        final Member writer) {
        this.title = title;
        this.contents = contents;
        this.capacity = capacity;
        this.isRecruit = isRecruit;
        this.isBoard = isBoard;
        this.writer = writer;
        this.addressTag = writer.getAddressTag();
        this.appliers = new HashSet<>();
        this.likes = new HashSet<>();
        this.comments = new HashSet<>();
    }

    public static ClassProgram of(final Member member,
                                  final ClassProgramRegisterRequest classProgramRegisterRequest) {
        ClassProgram classProgram = ClassProgram.builder()
                .title(classProgramRegisterRequest.getTitle())
                .contents(classProgramRegisterRequest.getContents())
                .capacity(classProgramRegisterRequest.getCapacity())
                .isRecruit(classProgramRegisterRequest.getIsRecruit())
                .isBoard(classProgramRegisterRequest.getIsBoard())
                .writer(member)
                .build();
        member.addClass(classProgram);

        return classProgram;
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

    public void verifyPermission(final Member member) {
        if (!this.writer.getId().equals(member.getId()) &&
            !member.getAuthority().substring(5).equals("ROLE")) {
            throw new NoPermissionOfClassProgram("No have permission to modify class program: " + this.id);
        }
    }

    public void modifiedFor(final ClassProgramModifiedRequest classProgramModifiedRequest) {
        changeContents(classProgramModifiedRequest.getContents());
        changeIsBoard(classProgramModifiedRequest.getIsBoard());
        changeIsRecruit(classProgramModifiedRequest.getIsRecruit());
        changeCapacity(classProgramModifiedRequest.getCapacity());
    }

    public void resetAssociated() {
        this.writer.removeWrittenClassProgram(this);
        this.appliers.forEach(applier -> applier.removeAppliedClassProgram(this));
        this.likes.forEach(like -> like.removeLikedClassProgram(this));
    }

    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

    private void changeContents(final String newContents) {
        if (newContents == null) {
            return;
        }
        this.contents = newContents;
    }

    private void changeIsBoard(final Boolean isBoard) {
        if (isBoard == null) {
            return;
        }
        this.isBoard = isBoard;
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
