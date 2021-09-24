package io.wisoft.poomi.domain.childminder.urgent.application;

import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentApplyRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "urgent_application_sequence_generator",
        sequenceName = "urgent_application_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class ChildminderUrgentApplication {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "urgent_application_sequence_generator"
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
            name = "urgent_id",
            referencedColumnName = "id"
    )
    private ChildminderUrgent childminderUrgent;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    private ChildminderUrgentApplication(final String contents,
                                         final Member writer,
                                         final ChildminderUrgent childminderUrgent) {
        this.contents = contents;
        this.writer = writer;
        this.childminderUrgent = childminderUrgent;
    }

    public static ChildminderUrgentApplication of(final ChildminderUrgentApplyRequest childminderUrgentApplyRequest,
                                                  final ChildminderUrgent childminderUrgent,
                                                  final Member member) {
        ChildminderUrgentApplication application = ChildminderUrgentApplication.builder()
                .contents(childminderUrgentApplyRequest.getContents())
                .writer(member)
                .childminderUrgent(childminderUrgent)
                .build();

        return application;
    }

}
