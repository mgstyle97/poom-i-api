package io.wisoft.poomi.domain.member.child;

import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.global.dto.request.member.ChildAddRequest;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "child_sequence_generator",
        sequenceName = "child_sequence",
        initialValue = 4,
        allocationSize = 1
)
public class Child {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "child_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "school")
    private String school;

    @Column(name = "special_note")
    private String specialNote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "expert_id",
            referencedColumnName = "id"
    )
    private ChildCareExpert caredExpertContent;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public Child(final String name, final Date birthday,
                 final String school, final String specialNote,
                 final Member parent) {
        this.name = name;
        this.birthday = birthday;
        this.school = school;
        this.specialNote = specialNote;
        this.parent = parent;
    }

    public static Child of(final ChildAddRequest childAddRequest, final Member parent) {
        return Child.builder()
                .name(childAddRequest.getName())
                .birthday(childAddRequest.getBirthday())
                .school(childAddRequest.getSchool())
                .specialNote(childAddRequest.getSpecialNote())
                .parent(parent)
                .build();
    }

    public void designateExpertContent(final ChildCareExpert childCareExpert) {
        this.caredExpertContent = childCareExpert;
    }

    public void removeExpertContent(final ChildCareExpert childCareExpert) {
        this.caredExpertContent = null;
    }

}
