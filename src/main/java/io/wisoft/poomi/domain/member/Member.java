package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.authority.Authority;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Entity
@SequenceGenerator(
        name = "member_sequence_generator",
        sequenceName = "member_sequence",
        initialValue = 4,
        allocationSize = 1
)
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nick")
    private String nick;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "score")
    private Integer score;

    @Column(name = "score_provider_count")
    private Integer scoreProviderCount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")}
    )
    private Set<Authority> authorities;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id"
    )
    private Address address;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "parent"
    )
    private Set<Child> children;

    @Embedded
    private ChildCareGroupProperties childCareGroupProperties;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "writer"
    )
    private Set<ChildCareExpert> writtenExperts;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "writer"
    )
    private Set<ChildCareExpertApply> expertApplies;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "likes"
    )
    private Set<ChildCareExpert> likedExpert;

    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

    @Builder
    public Member(final String name, final String phoneNumber,
                  final String email, final String password,
                  final String nick, final Integer age, final Gender gender,
                  final Set<Authority> authorities,
                  final Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.age = age;
        this.score = 0;
        this.scoreProviderCount = 0;
        this.gender = gender;
        this.authorities = authorities;
        this.address = address;
        this.children = new HashSet<>();
        this.childCareGroupProperties = new ChildCareGroupProperties();
        this.writtenExperts = new HashSet<>();
        this.expertApplies = new HashSet<>();
        this.likedExpert = new HashSet<>();
    }

    public static Member of(final SignupRequest signupRequest,
                            final PasswordEncoder passwordEncoder,
                            final Authority userAuthority) {
        Member member = Member.builder()
                .name(signupRequest.getName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nick(signupRequest.getNick())
                .age(signupRequest.getAge())
                .gender(signupRequest.getGender())
                .authorities(Collections.singleton(userAuthority))
                .build();
        return member;
    }

    public void checkChildInChildren(final Child child) {
        if (!this.children.contains(child)) {
            throw new IllegalArgumentException("해당 회원의 자식 정보가 아닙니다.");
        }
    }

    public void updateAddressInfo(final Address address) {
        this.address = address;
    }

    public void setChildren(final List<Child> children) {
        if (children == null) {
            return;
        }
        this.children.addAll(children);
    }

    public Authentication toAuthentication() {
        Collection<? extends GrantedAuthority> authorities = this.authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                this.email, this.password, authorities
        );
    }

    public String getAuthority() {
        return this.authorities.stream()
                .findFirst()
                .get()
                .getAuthority();
    }

    public Member setOAuthAccountName(final String name) {
        this.name = name;
        return this;
    }

    public void removeChild(final Child child) {
        if (!this.children.contains(child)) {
            throw new IllegalArgumentException("No child data in member object");
        }
        this.children.remove(child);
    }

    public AddressTag getAddressTag() {
        return this.address.getAddressTag();
    }

    public void addGroup(final ChildCareGroup childCareGroup) {
        Set<ChildCareGroup> writtenGroup = this.childCareGroupProperties.getWrittenGroup();
        writtenGroup.add(childCareGroup);
    }

    public void removeWrittenGroup(final ChildCareGroup childCareGroup) {
        this.childCareGroupProperties.getWrittenGroup().remove(childCareGroup);
    }

    public void removeLikedGroup(final ChildCareGroup childCareGroup) {
        this.childCareGroupProperties.getLikedGroup().remove(childCareGroup);
    }

    public void removeAppliedGroup(final ChildCareGroup childCareGroup) {
        this.childCareGroupProperties.getAppliedGroup().remove(childCareGroup);
    }

    public void addAppliedGroup(final ChildCareGroup childCareGroup) {
        Set<ChildCareGroup> appliedGroup = this.childCareGroupProperties.getAppliedGroup();
        appliedGroup.add(childCareGroup);
    }

    public void addLikedGroup(final ChildCareGroup childCareGroup) {
        Set<ChildCareGroup> likedGroup = this.childCareGroupProperties.getLikedGroup();
        likedGroup.add(childCareGroup);
    }

    public void addExpert(final ChildCareExpert childCareExpert) {
        this.writtenExperts.add(childCareExpert);
    }

    public void addApplication(final ChildCareExpertApply application) {
        this.expertApplies.add(application);
    }

    public void addLikedExpert(final ChildCareExpert childCareExpert) {
        this.likedExpert.add(childCareExpert);
    }

    public void removeLikedExpert(final ChildCareExpert childCareExpert) {
        this.likedExpert.remove(childCareExpert);
    }

    public void removeApplication(final ChildCareExpertApply application) {
        this.expertApplies.remove(application);
    }

    public void removeWrittenExpert(final ChildCareExpert childCareExpert) {
        this.writtenExperts.remove(childCareExpert);
    }
}
