package io.wisoft.poomi.domain.member.child;

import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.participating.GroupParticipatingMember;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.global.dto.request.member.ChildAddRequest;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "child_sequence_generator",
        sequenceName = "child_sequence",
        initialValue = 8,
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member parent;

    @OneToMany(mappedBy = "child", fetch = FetchType.EAGER)
    private Set<GroupParticipatingMember> participatingGroups;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "caringChild")
    private ChildCareExpert caredExpertContent;

    @Override
    public int hashCode() {
        return this.id.intValue();
    }

    @Builder
    public Child(final String name, final Date birthday,
                 final String school, final String specialNote,
                 final Gender gender, final Member parent) {
        this.name = name;
        this.birthday = birthday;
        this.school = school;
        this.specialNote = specialNote;
        this.gender = gender;
        this.parent = parent;
        this.participatingGroups = new HashSet<>();
    }

    public static Child of(final ChildAddRequest childAddRequest, final Member parent) {
        return Child.builder()
                .name(childAddRequest.getName())
                .birthday(childAddRequest.getBirthday())
                .school(childAddRequest.getSchool())
                .specialNote(childAddRequest.getSpecialNote())
                .gender(childAddRequest.getGender())
                .parent(parent)
                .build();
    }

    public void designateExpertContent(final ChildCareExpert childCareExpert) {
        this.caredExpertContent = childCareExpert;
    }

    public void cancelExpertContent() {
        this.caredExpertContent = null;
    }

    public void removeExpertContent(final ChildCareExpert childCareExpert) {
        this.caredExpertContent = null;
    }

    public void addParticipatingGroup(final GroupParticipatingMember participatingGruop) {
        this.participatingGroups.add(participatingGruop);
    }

    public void withdrawFromGroup(final ChildCareGroup group) {
        this.participatingGroups.stream()
                .filter(groupParticipatingMember -> groupParticipatingMember.getGroup().equals(group))
                .forEach(this.participatingGroups::remove);
    }

}
