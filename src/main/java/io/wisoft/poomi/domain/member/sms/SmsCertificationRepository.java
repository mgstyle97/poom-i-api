package io.wisoft.poomi.domain.member.sms;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCertificationRepository extends JpaRepository<SmsCertification, Long> {

    Optional<SmsCertification> findByPhoneNumber(String phoneNumber);

}
