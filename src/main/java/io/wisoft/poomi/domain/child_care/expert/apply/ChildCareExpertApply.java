package io.wisoft.poomi.domain.child_care.expert.apply;

import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertApplyRequest;
import io.wisoft.poomi.global.exception.exceptions.NoPermissionOfContentException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Optional;

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
            name = "writer_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "child_id",
            referencedColumnName = "id"
    )
    private Child child;

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
                                 final Member writer, final Child child,
                                 final ChildCareExpert childCareExpert) {
        this.contents = contents;
        this.writer = writer;
        this.child = child;
        this.childCareExpert = childCareExpert;
    }

    public static ChildCareExpertApply of(final ChildCareExpertApplyRequest childCareExpertApplyRequest,
                                          final ChildCareExpert childCareExpert,
                                          final Member member, final Child child) {
        ChildCareExpertApply expertApply = ChildCareExpertApply.builder()
                .contents(childCareExpertApplyRequest.getContents())
                .writer(member)
                .child(child)
                .childCareExpert(childCareExpert)
                .build();

        return expertApply;
    }

    public void checkWriter(final Member member) {
        if (!this.writer.equals(member)) {
            throw new NoPermissionOfContentException();
        }
    }

    public void modifiedByRequest(final String contents, Child child) {
        changeContents(contents);
        changeChild(Optional.ofNullable(child));
    }

    public void reset() {
        this.writer.removeExpertApply(this);
    }

    private void changeContents(final String contents) {
        if (StringUtils.hasText(contents) && !this.contents.equals(contents)) {
            this.contents = contents;
        }
    }

    private void changeChild(final Optional<Child> optionalChild) {
        optionalChild.ifPresent(child -> {
            this.child = child;
        });
    }

}
