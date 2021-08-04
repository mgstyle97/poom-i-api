package io.wisoft.poomi.configures.security.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "refresh_token_sequence_generator",
        sequenceName = "refresh_token_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "refresh_token_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "member_login_id")
    private String loginId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public RefreshToken(String memberId, String refreshToken) {
        this.loginId = memberId;
        this.refreshToken = refreshToken;
    }

}