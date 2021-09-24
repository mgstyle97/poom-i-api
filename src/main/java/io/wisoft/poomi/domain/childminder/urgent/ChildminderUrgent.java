package io.wisoft.poomi.domain.childminder.urgent;

import io.wisoft.poomi.domain.childminder.urgent.application.ChildminderUrgentApplication;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.childminder.BaseChildminderEntity;
import io.wisoft.poomi.domain.member.address.AddressTag;
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
        name = "urgent_sequence_generator",
        sequenceName = "urgent_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class ChildminderUrgent extends BaseChildminderEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "urgent_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(
            name = "contents",
            columnDefinition = "TEXT"
    )
    private String contents;

    @Column(name = "is_recruit")
    private Boolean isRecruit;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "childminderUrgent")
    private Set<ChildminderUrgentApplication> applications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "childminderUrgent")
    private Set<Child> childrenToHelp;

    @Builder
    public ChildminderUrgent(final String contents, final Boolean isRecruit,
                             final LocalDateTime startTime, final LocalDateTime endTime,
                             final Member writer) {
        this.contents = contents;
        this.isRecruit = isRecruit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.applications = new HashSet<>();
        this.childrenToHelp = new HashSet<>();
        setWriter(writer);
        setAddressTag(writer.getAddressTag());
    }

    public static ChildminderUrgent of(final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
                                       final Member member, final Child child) {
        ChildminderUrgent childminderUrgent = ChildminderUrgent.builder()
                .contents(childminderUrgentRegisterRequest.getContents())
                .isRecruit(childminderUrgentRegisterRequest.getIsRecruit())
                .startTime(childminderUrgentRegisterRequest.getStartTime())
                .endTime(childminderUrgentRegisterRequest.getEndTime())
                .writer(member)
                .build();
        childminderUrgent.addChildToHelp(Optional.ofNullable(child));
        member.addUrgent(childminderUrgent);

        return childminderUrgent;
    }

    public void addChildToHelp(final Optional<Child> optionalChild) {
        optionalChild.ifPresent(child -> this.childrenToHelp.add(child));
    }

    public void modifiedFor(final ChildminderUrgentModifiedRequest childminderUrgentModifiedRequest) {
        changeContents(childminderUrgentModifiedRequest.getContents());
        changeIsRecruit(childminderUrgentModifiedRequest.getIsRecruit());
        changeStartTime(childminderUrgentModifiedRequest.getStartTime());
        changeEndTime(childminderUrgentModifiedRequest.getEndTime());
    }

    public void isWriter(final Member member) {
        if (getWriter().equals(member)) {
            throw new IllegalArgumentException("작성자는 지원할 수 없습니다.");
        }
    }

    public void addApplication(final ChildminderUrgentApplication childminderUrgentApplication) {
        this.applications.add(childminderUrgentApplication);
    }

    private void changeContents(final String contents) {
        if(!StringUtils.hasText(contents)) {
            return;
        }
        this.contents = contents;
    }

    private void changeIsRecruit(final Boolean isRecruit) {
        if (this.isRecruit.equals(isRecruit)) {
            return;
        }
        this.isRecruit = isRecruit;
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
