package io.wisoft.poomi.domain.member.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "sms_certification_sequence_generator",
        sequenceName = "sms_certification_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "sms_certification")
public class SmsCertification {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sms_certification_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "certification_number")
    private String certificationNumber;

    @Builder
    public SmsCertification(String phoneNumber, String certificationNumber) {
        this.phoneNumber = phoneNumber;
        this.certificationNumber = certificationNumber;
    }

    public void verifyCertificationNumber(String certificationNumber) {
        if (!this.certificationNumber.equals(certificationNumber)) {
            throw new IllegalArgumentException();
        }
    }

}
