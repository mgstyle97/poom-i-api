package io.wisoft.poomi.domain.auth.property;

import io.wisoft.poomi.domain.auth.BaseCertificationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BasePropertyValidationEntity extends BaseCertificationEntity {

    @Column(name = "certification_number")
    private String certificationNumber;

    protected BasePropertyValidationEntity(final String certificationNumber,
                                           final String expiredValidationToken) {
        super(expiredValidationToken);
        this.certificationNumber = certificationNumber;
    }

}
