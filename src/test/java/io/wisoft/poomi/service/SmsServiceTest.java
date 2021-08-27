package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsServiceTest {

    @Autowired
    private SmsService smsService;

    @Test
    @Disabled
    void sms_test() throws URISyntaxException, JsonProcessingException {
        smsService.sendSms("01075976959");
    }

}