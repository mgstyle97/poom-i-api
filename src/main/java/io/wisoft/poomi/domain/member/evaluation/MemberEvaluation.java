package io.wisoft.poomi.domain.member.evaluation;

import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "member_evaluation_sequence_generator",
        sequenceName = "member_evaluation_sequence",
        initialValue = 4,
        allocationSize = 1
)
@Table(name = "member_evaluation")
public class MemberEvaluation {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_evaluation_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private Integer score;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member member;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public MemberEvaluation(final Integer score, final Member member) {
        this.score = score;
        this.member = member;
    }

}
