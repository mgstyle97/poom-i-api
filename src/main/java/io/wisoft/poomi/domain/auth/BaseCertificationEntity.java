package io.wisoft.poomi.domain.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseCertificationEntity {

    @Column(name = "certification_number")
    private String certificationNumber;

}
