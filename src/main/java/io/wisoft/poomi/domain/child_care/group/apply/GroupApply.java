package io.wisoft.poomi.domain.child_care.group.apply;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupApplyRequest;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "group_apply_sequence_generator",
        sequenceName = "group_apply_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "group_apply")
public class GroupApply {

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

    @Builder
    public GroupApply(final String contents, final Child child,
                      final Member writer, final ChildCareGroup childCareGroup) {
        this.contents = contents;
        this.child = child;
        this.writer = writer;
        this.childCareGroup = childCareGroup;
    }

    public static GroupApply of(final ChildCareGroupApplyRequest applyRequest, final Child child,
                                final Member writer, final ChildCareGroup childCareGroup) {
        return GroupApply.builder()
                .contents(applyRequest.getContents())
                .child(child)
                .writer(writer)
                .childCareGroup(childCareGroup)
                .build();
    }

}
