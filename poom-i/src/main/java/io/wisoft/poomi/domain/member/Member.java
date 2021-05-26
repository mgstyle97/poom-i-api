package io.wisoft.poomi.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.wisoft.poomi.domain.member.dto.MemberJoinDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@SequenceGenerator(
        name = "member_sequence_generator",
        sequenceName = "member_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence_generator"
    )
    @Column(name = "member_id")
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String phoneNumber;

    @NonNull
    @Column(unique = true)
    private String email;

    @NonNull
    @JsonIgnore
    private String password;

    @NonNull
    @Column(unique = true)
    private String nick;

    @NonNull
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime recentLoginAt;

    private boolean isBabysitter;

    @Column(unique = true)
    private String lineId;

    public static Member of(final MemberJoinDto memberJoinDto) {
        return Member.builder()
                .name(memberJoinDto.getName())
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .nick(memberJoinDto.getNick())
                .address(memberJoinDto.getAddress())
                .createdAt(LocalDateTime.now())
                .isBabysitter(memberJoinDto.isBabysitter())
                .build();
    }

    public static boolean checkPassword(final Member member, final String inputPassword) {
        return (member.getPassword().equals(inputPassword));
    }

    public Member changeName(final String name) {
        if ((name != null) &&
                (!this.name.equals(name))) {
            this.name = name;
        }

        return this;
    }

    public Member changePhoneNumber(final String phoneNumber) {
        if ((phoneNumber != null) &&
                (!this.phoneNumber.equals(phoneNumber))) {
            this.phoneNumber = phoneNumber;
        }

        return this;
    }

    public Member changePassword(final String password) {
        if ((password != null) &&
                (!this.password.equals(password))) {
            this.password = password;
        }

        return this;
    }

    public Member changeNick(final String nick) {
        if ((nick != null) &&
                (!this.nick.equals(nick))) {
            this.nick = nick;
        }

        return this;
    }

    public Member changeAddress(final String address) {
        if ((address != null) &&
                (!this.address.equals(address))) {
            this.address = address;
        }

        return this;
    }

    public Member changeIsBabysitter(final boolean isBabysitter) {
        if (this.isBabysitter != isBabysitter) {
            this.isBabysitter = isBabysitter;
        }

        return this;
    }

}
