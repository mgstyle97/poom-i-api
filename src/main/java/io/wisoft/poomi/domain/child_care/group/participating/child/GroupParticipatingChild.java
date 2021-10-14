package io.wisoft.poomi.domain.child_care.group.participating.child;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "group_participating_child_sequence_generator",
        sequenceName = "group_participating_child_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "group_participating_child")
public class GroupParticipatingChild {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_participating_child_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "child_id",
            referencedColumnName = "id"
    )
    private Child child;

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
    public GroupParticipatingChild(final Child child, final ChildCareGroup group) {
        this.child = child;
        this.childCareGroup = group;
    }

}
