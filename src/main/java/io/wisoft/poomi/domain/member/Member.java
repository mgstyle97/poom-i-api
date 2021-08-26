package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.bind.request.ChildAddRequest;
import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.domain.member.cmInfo.ChildminderInfo;
import io.wisoft.poomi.domain.member.authority.Authority;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Entity
@SequenceGenerator(
        name = "member_sequence_generator",
        sequenceName = "member_sequence",
        initialValue = 1,
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
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "child_id",
            referencedColumnName = "id"
    )
    private List<Child> children;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "cm_info_id",
            referencedColumnName = "id"
    )
    private ChildminderInfo childminderInfo;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private List<ClassProgram> writtenClasses;

    @ManyToMany(mappedBy = "appliers", fetch = FetchType.LAZY)
    private List<ClassProgram> appliedClasses;

    public Member() {
        this.writtenClasses = new ArrayList<>();
        this.appliedClasses = new ArrayList<>();
    }

    @Builder
    public Member(final String name, final String phoneNumber,
                  final String email, final String password,
                  final String nick, final Set<Authority> authorities,
                  final Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.authorities = authorities;
        this.address = address;
        this.children = new ArrayList<>();
        this.writtenClasses = new ArrayList<>();
        this.appliedClasses = new ArrayList<>();
    }

    public static Member of(final SignupRequest signupRequest,
                            final PasswordEncoder passwordEncoder,
                            final AuthorityRepository authorityRepository) {
        Member member = Member.builder()
                .name(signupRequest.getName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nick(signupRequest.getNick())
                .authorities(Collections.singleton(authorityRepository.getUserAuthority()))
                .build();
        return member;
    }

    public void updateAddressInfo(final Address address) {
        this.address = address;
    }

    public void updateChildminderInfo(final ChildminderInfo childminderInfo) {
        this.childminderInfo = childminderInfo;
    }

    public void setChildren(final List<ChildAddRequest> children, final ChildRepository childRepository) {
        if (children == null) {
            return;
        }
        children.stream()
                .map(childAddRequest -> Child.of(childAddRequest, childRepository))
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

    public void addClass(final ClassProgram classProgram) {

        if (!this.writtenClasses.contains(classProgram)) {
            this.writtenClasses.add(classProgram);
        }
    }

    public void addAppliedClass(final ClassProgram classProgram) {

        if (!this.appliedClasses.contains(classProgram)) {
            this.appliedClasses.add(classProgram);
        }
    }

}
