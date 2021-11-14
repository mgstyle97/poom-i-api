package io.wisoft.poomi.domain.child_care.group.participating;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.apply.GroupApply;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.response.child_care.group.ParticipationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "group_participating_member_sequence_generator",
        sequenceName = "group_participating_member_sequence",
        initialValue = 11,
        allocationSize = 1
)
@Table(name = "group_participating_member")
public class GroupParticipatingMember {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_participating_member_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member member;

    @ManyToOne
    @JoinColumn(
            name = "child_id",
            referencedColumnName = "id"
    )
    private Child child;

    @ManyToOne
    @JoinColumn(
            name = "group_id",
            referencedColumnName = "id"
    )
    private ChildCareGroup group;

    @Enumerated(EnumType.STRING)
    @Column(name = "participation_type")
    private ParticipationType participationType;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public GroupParticipatingMember(final Member member, final Child child,
                                    final ChildCareGroup group, final ParticipationType participationType) {
        this.member = member;
        this.child = child;
        this.group = group;
        this.participationType = participationType;
    }

    public static GroupParticipatingMember of(final GroupApply apply, final ParticipationType participationType) {
        return GroupParticipatingMember.builder()
                .member(apply.getWriter())
                .child(apply.getChild())
                .group(apply.getChildCareGroup())
                .participationType(participationType)
                .build();
    }

}
