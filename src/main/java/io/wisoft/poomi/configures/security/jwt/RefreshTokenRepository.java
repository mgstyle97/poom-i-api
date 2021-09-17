package io.wisoft.poomi.configures.security.jwt;

import io.wisoft.poomi.configures.security.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMemberEmail(final String memberEmail);

}
