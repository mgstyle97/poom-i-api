package io.wisoft.poomi.domain.child_care.expert;

import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.child_care.BaseChildCareEntity;
import io.wisoft.poomi.global.exception.exceptions.AlreadyRequestException;
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

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "childCareExpert")
    private Set<ChildCareExpertApply> applications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "childCareExpert")
    private Set<Child> childrenToHelp;

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
        this.contents = contents;
        this.recruitType = recruitType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.applications = new HashSet<>();
        this.childrenToHelp = new HashSet<>();
        this.likes = new HashSet<>();
        setWriter(writer);
        setAddressTag(writer.getAddressTag());
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
        childCareExpert.addChildToHelp(Optional.ofNullable(child));
        member.addExpert(childCareExpert);

        return childCareExpert;
    }

    public void addChildToHelp(final Optional<Child> optionalChild) {
        optionalChild.ifPresent(child -> this.childrenToHelp.add(child));
    }

    public void modifiedFor(final ChildCareExpertModifiedRequest childCareExpertModifiedRequest) {
        changeContents(childCareExpertModifiedRequest.getContents());
        changeIsRecruit(childCareExpertModifiedRequest.getRecruitType());
        changeStartTime(childCareExpertModifiedRequest.getStartTime());
        changeEndTime(childCareExpertModifiedRequest.getEndTime());
    }

    public void isWriter(final Member member) {
        if (getWriter().equals(member)) {
            throw new IllegalArgumentException("작성자는 지원할 수 없습니다.");
        }
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
        member.addLikedExpert(this);
    }

    public void resetAssociated() {
        this.likes.forEach(like -> like.removeLikedExpert(this));
        this.applications.forEach(ChildCareExpertApply::reset);

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
