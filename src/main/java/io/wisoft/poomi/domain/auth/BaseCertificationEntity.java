package io.wisoft.poomi.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class BaseCertificationEntity {

    @Column(name = "expired_validation_token")
    private String expiredValidationToken;

}
