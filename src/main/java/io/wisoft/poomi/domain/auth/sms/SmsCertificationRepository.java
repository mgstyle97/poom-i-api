package io.wisoft.poomi.domain.auth.sms;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCertificationRepository extends JpaRepository<SmsCertification, Long> {

    Optional<SmsCertification> findByPhoneNumber(final String phoneNumber);

    default SmsCertification getByPhoneNumber(final String phoneNumber) {
        return this.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new NotFoundEntityDataException("phone number: " + phoneNumber + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
