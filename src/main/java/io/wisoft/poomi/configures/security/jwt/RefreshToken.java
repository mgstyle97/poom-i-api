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

    @Column(name = "member_email")
    private String email;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public RefreshToken(final String email, final String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

}
