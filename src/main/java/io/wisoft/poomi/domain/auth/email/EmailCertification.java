package io.wisoft.poomi.domain.auth.email;

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
        name = "email_certification_sequence_generator",
        sequenceName = "email_certification_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class EmailCertification extends BaseCertificationEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "email_certification_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private String email;

    @Builder
    private EmailCertification(final String email,
                              final String certificationNumber) {
        this.email = email;
        setCertificationNumber(certificationNumber);
    }

    public static EmailCertification of(final String email,
                                        final String certificationNumber) {
        return EmailCertification.builder()
                .email(email)
                .certificationNumber(certificationNumber)
                .build();
    }

    public void verifyCertificationNumber(final String certificationNumber) {
        if (!getCertificationNumber().equals(certificationNumber)) {
            throw new NoMatchCertificationNumberException();
        }
    }

}
