package io.wisoft.poomi.domain.child_care.group;

import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.child_care.group.apply.GroupApply;
import io.wisoft.poomi.domain.child_care.group.participating.member.GroupParticipatingMember;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.child_care.BaseChildCareEntity;
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

    @Column(name = "regular_meeting_day")
    private String regularMeetingDay;

    @Column(name = "main_activity")
    private String mainActivity;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "childCareGroup")
    private Set<GroupParticipatingMember> participatingMembers;

    @OneToMany(mappedBy = "childCareGroup", fetch = FetchType.LAZY)
    private Set<GroupApply> applies;

    @Builder
    public ChildCareGroup(final String title, final String regularMeetingDay,
                          final String mainActivity,  final String description,
                          final Member writer,
                          final RecruitmentStatus recruitmentStatus) {
        super(writer, writer.getAddressTag(), recruitmentStatus);
        this.title = title;
        this.regularMeetingDay = regularMeetingDay;
        this.mainActivity = mainActivity;
        this.description = description;
        this.participatingMembers = new HashSet<>();
        this.applies = new HashSet<>();
    }

    public static ChildCareGroup of(final Member member,
                                    final ChildCareGroupRegisterRequest childCareGroupRegisterRequest) {
        ChildCareGroup childCareGroup = ChildCareGroup.builder()
                .title(childCareGroupRegisterRequest.getTitle())
                .regularMeetingDay(childCareGroupRegisterRequest.getRegularMeetingDay())
                .mainActivity(childCareGroupRegisterRequest.getMainActivity())
                .description(childCareGroupRegisterRequest.getDescription())
                .recruitmentStatus(childCareGroupRegisterRequest.getRecruitmentStatus())
                .writer(member)
                .build();

        return childCareGroup;
    }

    public void addParticipatingMember(final GroupParticipatingMember participatingMember) {
        this.participatingMembers.add(participatingMember);
    }

    public void addApplier(final GroupApply apply) {
        this.applies.add(apply);
    }

    public void modifiedFor(final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        changeTitle(childCareGroupModifiedRequest.getTitle());
        changeDescription(childCareGroupModifiedRequest.getDescription());
        changeMainActivity(childCareGroupModifiedRequest.getMainActivity());
        changeRegularMeetingDay(childCareGroupModifiedRequest.getRegularMeetingDay());
        super.changeRecruitmentStatus(childCareGroupModifiedRequest.getRecruitmentStatus());
    }

    public void resetAssociated() {
        getWriter().removeWrittenGroup(this);
    }

    private void changeTitle(final String title) {
        if (StringUtils.hasText(title) && !this.title.equals(title)) {
            this.title = title;
        }
    }

    private void changeRegularMeetingDay(final String regularMeetingDay) {
        if (StringUtils.hasText(regularMeetingDay) && !this.regularMeetingDay.equals(regularMeetingDay)) {
            this.regularMeetingDay = regularMeetingDay;
        }
    }

    private void changeMainActivity(final String mainActivity) {
        if (StringUtils.hasText(mainActivity) && !this.mainActivity.equals(mainActivity)) {
            this.mainActivity = mainActivity;
        }
    }

    private void changeDescription(final String description) {
        if (StringUtils.hasText(description) && !this.description.equals(description)) {
            this.description = description;
        }
    }

}
