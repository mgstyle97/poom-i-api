package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.common.error.exceptions.WrongMemberPasswordException;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.domain.member.cmInfo.ChildminderInfo;
import io.wisoft.poomi.domain.member.enumerate.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "login_id")
    private String loginId;

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

    public static Member of(SignupRequest joinRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        BeanUtils.copyProperties(joinRequest, member);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setAuthorities(Collections.singleton(new Authority(1L, "ROLE_USER")));
        return member;
    }

    public Member checkPassword(String confirmPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(confirmPassword, this.password)) {
            throw new WrongMemberPasswordException("Wrong password");
        }

        return this;
    }

}
