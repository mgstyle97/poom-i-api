package io.wisoft.poomi.domain.auth.sms;

import io.wisoft.poomi.domain.auth.BaseCertificationEntity;
import io.wisoft.poomi.global.exception.exceptions.NoMatchCertificationNumberException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "sms_certification_sequence_generator",
        sequenceName = "sms_certification_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "sms_certification")
public class SmsCertification extends BaseCertificationEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sms_certification_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Builder
    private SmsCertification(final String phoneNumber,
                            final String certificationNumber) {
        this.phoneNumber = phoneNumber;
        setCertificationNumber(certificationNumber);
    }

    public static SmsCertification of(final String phoneNumber,
                                      final String certificationNumber) {
        return SmsCertification.builder()
                .phoneNumber(phoneNumber)
                .certificationNumber(certificationNumber)
                .build();
    }

    public void verifyCertificationNumber(final String certificationNumber) {
        if (!getCertificationNumber().equals(certificationNumber)) {
            throw new NoMatchCertificationNumberException();
        }
    }

}
