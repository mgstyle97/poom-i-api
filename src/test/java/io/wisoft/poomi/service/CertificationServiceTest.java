package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.service.certification.CertificationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

@SpringBootTest
class CertificationServiceTest {

    @Autowired
    private CertificationService certificationService;

    @Test
    @Disabled
    void sms_test() throws URISyntaxException, JsonProcessingException {
    }

}