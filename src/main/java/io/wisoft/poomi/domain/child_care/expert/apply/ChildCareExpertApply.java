package io.wisoft.poomi.domain.child_care.expert.apply;

import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertApplyRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "expert_apply_sequence_generator",
        sequenceName = "expert_apply_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "expert_apply")
public class ChildCareExpertApply {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expert_apply_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "expert_id",
            referencedColumnName = "id"
    )
    private ChildCareExpert childCareExpert;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    private ChildCareExpertApply(final String contents,
                                 final Member writer,
                                 final ChildCareExpert childCareExpert) {
        this.contents = contents;
        this.writer = writer;
        this.childCareExpert = childCareExpert;
    }

    public static ChildCareExpertApply of(final ChildCareExpertApplyRequest childCareExpertApplyRequest,
                                          final ChildCareExpert childCareExpert,
                                          final Member member) {
        ChildCareExpertApply application = ChildCareExpertApply.builder()
                .contents(childCareExpertApplyRequest.getContents())
                .writer(member)
                .childCareExpert(childCareExpert)
                .build();

        return application;
    }

    public void reset() {
        this.writer.removeApplication(this);
    }

}
