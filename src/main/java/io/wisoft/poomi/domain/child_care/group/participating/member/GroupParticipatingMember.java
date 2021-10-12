package io.wisoft.poomi.domain.child_care.group.participating.member;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.member.Member;
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
        initialValue = 1,
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

    @Enumerated(EnumType.STRING)
    @Column(name = "participating_type")
    private ParticipatingType participatingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "group_id",
            referencedColumnName = "id"
    )
    private ChildCareGroup childCareGroup;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public GroupParticipatingMember(final ParticipatingType participatingType,
                                    final Member member, final ChildCareGroup childCareGroup) {
        this.participatingType = participatingType;
        this.member = member;
        this.childCareGroup = childCareGroup;
    }

}
