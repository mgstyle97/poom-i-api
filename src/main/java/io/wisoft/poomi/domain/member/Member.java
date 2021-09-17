package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.global.dto.request.member.ChildAddRequest;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.cmInfo.ChildminderInfo;
import io.wisoft.poomi.domain.member.authority.Authority;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
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
        initialValue = 2,
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "cm_info_id",
            referencedColumnName = "id"
    )
    private ChildminderInfo childminderInfo;

    @Embedded
    private ChildminderClassProperties childminderClassProperties;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "writer"
    )
    private Set<ChildminderUrgent> writtenUrgents;

    @Builder
    public Member(final String name, final String phoneNumber,
                  final String email, final String password,
                  final String nick, final Gender gender,
                  final Set<Authority> authorities,
                  final Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.gender = gender;
        this.authorities = authorities;
        this.address = address;
        this.children = new HashSet<>();
        this.childminderClassProperties = new ChildminderClassProperties();
        this.writtenUrgents = new HashSet<>();
    }

    @Override
    public int hashCode() {
        return this.getId().intValue();
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
                .gender(Gender.getGender(signupRequest.getGender()))
                .authorities(Collections.singleton(userAuthority))
                .build();
        return member;
    }

    public void updateAddressInfo(final Address address) {
        this.address = address;
    }

    public void updateChildminderInfo(final ChildminderInfo childminderInfo) {
        this.childminderInfo = childminderInfo;
    }

    public void setChildren(final List<ChildAddRequest> children) {
        if (children == null) {
            return;
        }
        children.stream()
                .map(childAddRequest -> Child.of(childAddRequest, this))
                .forEach(this.children::add);
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

    public void addClass(final ChildminderClass childminderClass) {
        Set<ChildminderClass> writtenClasses = this.childminderClassProperties.getWrittenClasses();
        writtenClasses.add(childminderClass);
    }

    public void removeWrittenClassProgram(final ChildminderClass childminderClass) {
        this.childminderClassProperties.getWrittenClasses().remove(childminderClass);
    }

    public void removeLikedClassProgram(final ChildminderClass childminderClass) {
        this.childminderClassProperties.getLikedClasses().remove(childminderClass);
    }

    public void removeAppliedClassProgram(final ChildminderClass childminderClass) {
        this.childminderClassProperties.getAppliedClasses().remove(childminderClass);
    }

    public void addAppliedClass(final ChildminderClass childminderClass) {
        Set<ChildminderClass> appliedClasses = this.childminderClassProperties.getAppliedClasses();
        appliedClasses.add(childminderClass);
    }

    public void addLikedClass(final ChildminderClass childminderClass) {
        Set<ChildminderClass> likedClasses = this.childminderClassProperties.getLikedClasses();
        likedClasses.add(childminderClass);
    }

    public void addUrgent(final ChildminderUrgent childminderUrgent) {
        this.writtenUrgents.add(childminderUrgent);
    }

}
