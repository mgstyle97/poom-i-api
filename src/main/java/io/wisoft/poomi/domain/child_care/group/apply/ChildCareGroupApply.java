package io.wisoft.poomi.domain.child_care.group.apply;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "group_apply_sequence_generator",
        sequenceName = "group_apply_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "group_apply")
public class ChildCareGroupApply {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_apply_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "child_id",
            referencedColumnName = "id"
    )
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "writer_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "group_id",
            referencedColumnName = "id"
    )
    private ChildCareGroup childCareGroup;

}
