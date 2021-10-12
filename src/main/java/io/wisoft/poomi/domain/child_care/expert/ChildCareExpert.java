package io.wisoft.poomi.domain.child_care.expert;

import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.child_care.BaseChildCareEntity;
import io.wisoft.poomi.global.exception.exceptions.AlreadyRequestException;
import io.wisoft.poomi.global.exception.exceptions.NoPermissionOfContentException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "expert_sequence_generator",
        sequenceName = "expert_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class ChildCareExpert extends BaseChildCareEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expert_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(
            name = "contents",
            columnDefinition = "TEXT"
    )
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruit_type")
    private RecruitType recruitType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "manager_id",
            referencedColumnName = "id"
    )
    private Member manager;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "childCareExpert")
    private Set<ChildCareExpertApply> applications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "caredExpertContent")
    private Set<Child> caringChildren;

    @ManyToMany(
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "expert_likes",
            joinColumns = {@JoinColumn(name = "expert_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")}
    )
    private Set<Member> likes;

    @Builder
    public ChildCareExpert(final String contents, final RecruitType recruitType,
                           final LocalDateTime startTime, final LocalDateTime endTime,
                           final Member writer) {
        super(writer, writer.getAddressTag(), RecruitmentStatus.RECRUITING);
        this.contents = contents;
        this.recruitType = recruitType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.applications = new HashSet<>();
        this.caringChildren = new HashSet<>();
        this.likes = new HashSet<>();
    }

    public static ChildCareExpert of(final ChildCareExpertRegisterRequest childCareExpertRegisterRequest,
                                     final Member member, final Child child) {
        ChildCareExpert childCareExpert = ChildCareExpert.builder()
                .contents(childCareExpertRegisterRequest.getContents())
                .recruitType(childCareExpertRegisterRequest.getRecruitType())
                .startTime(childCareExpertRegisterRequest.getStartTime())
                .endTime(childCareExpertRegisterRequest.getEndTime())
                .writer(member)
                .build();
        childCareExpert.addChildToCaringChildren(Optional.ofNullable(child));
        member.addWrittenExpertContent(childCareExpert);

        if (childCareExpert.getRecruitType().equals(RecruitType.VOLUNTEER)) {
            childCareExpert.setManager(member);
            member.manageOfExpertContent(childCareExpert);
        }

        return childCareExpert;
    }

    public void addChildToCaringChildren(final Optional<Child> optionalChild) {
        optionalChild.ifPresent(child -> {
            this.caringChildren.add(child);
            child.designateExpertContent(this);
        });
    }

    public void setManager(final Member member) {
        this.manager = member;
    }

    public void modifiedFor(final ChildCareExpertModifiedRequest childCareExpertModifiedRequest,
                            final Child child) {
        changeContents(childCareExpertModifiedRequest.getContents());
        changeIsRecruit(childCareExpertModifiedRequest.getRecruitType());
        changeCaringChild(child);
        changeStartTime(childCareExpertModifiedRequest.getStartTime());
        changeEndTime(childCareExpertModifiedRequest.getEndTime());
    }

    public void isAlreadyApplier(final Member member) {
        boolean isNoneMatchMember = this.applications.stream()
                .map(ChildCareExpertApply::getWriter)
                .noneMatch(applier -> applier.equals(member));
        if (!isNoneMatchMember) {
            throw new AlreadyRequestException();
        }
    }

    public void addApplication(final ChildCareExpertApply childCareExpertApply) {
        this.applications.add(childCareExpertApply);
    }

    public void addLikes(final Member member) {
        this.likes.add(member);
        member.addLikedExpertContent(this);
    }

    public void resetAssociated() {
        this.likes.forEach(like -> like.removeLikedExpertContent(this));
        this.applications.forEach(ChildCareExpertApply::reset);
        this.caringChildren.forEach(child -> child.removeExpertContent(this));

    }

    public void checkApplyIncluded(final ChildCareExpertApply expertApply) {
        if (!this.applications.contains(expertApply)) {
            throw new IllegalArgumentException("expert id: " + this.id + "에 존재하지 않는 요청입니다.");
        }
    }

    public void approveApply(final ChildCareExpertApply expertApply) {
        addChildToCaringChildren(Optional.ofNullable(expertApply.getChild()));

        if (this.recruitType.equals(RecruitType.RECRUIT)) {
            setManager(expertApply.getWriter());
        }
    }

    public void removeApply(final ChildCareExpertApply expertApply) {
        this.applications.remove(expertApply);
    }

    private void changeContents(final String contents) {
        if(!StringUtils.hasText(contents)) {
            return;
        }
        this.contents = contents;
    }

    private void changeIsRecruit(final RecruitType recruitType) {
        if (this.recruitType.equals(recruitType)) {
            return;
        }
        this.recruitType = recruitType;
    }

    private void changeCaringChild(final Child child) {
        this.caringChildren.stream()
                .findFirst()
                .ifPresent(Child::cancelExpertContent);
        this.caringChildren.add(child);
    }

    private void changeStartTime(final LocalDateTime startTime) {
        if(this.startTime.equals(startTime)) {
            return;
        }

        this.startTime = startTime;
    }

    private void changeEndTime(final LocalDateTime endTime) {
        if (this.endTime.equals(endTime)) {
            return;
        }

        this.endTime = endTime;
    }

}
