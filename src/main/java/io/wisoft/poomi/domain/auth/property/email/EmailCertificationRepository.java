package io.wisoft.poomi.domain.auth.property.email;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {

    Optional<EmailCertification> findByEmail(final String email);

    default EmailCertification getByEmail(final String email) {
        return this.findByEmail(email)
                .orElseThrow(
                        () -> new NotFoundEntityDataException("email: " + email + "에 관한 데이터를 찾지 못했습니다.")
                );
    }

}
