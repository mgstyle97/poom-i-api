package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.service.certification.PropertyCertificationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

@SpringBootTest
class PropertyCertificationServiceTest {

    @Autowired
    private PropertyCertificationService propertyCertificationService;

    @Test
    @Disabled
    void sms_test() throws URISyntaxException, JsonProcessingException {
    }

}