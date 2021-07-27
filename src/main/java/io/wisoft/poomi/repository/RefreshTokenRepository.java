package io.wisoft.poomi.repository;

import io.wisoft.poomi.configures.security.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
