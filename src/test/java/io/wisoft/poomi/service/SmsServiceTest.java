package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.bind.dto.SmsResultDto;
import io.wisoft.poomi.bind.request.MessageRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsServiceTest {

    @Autowired
    private SmsService service;

    @Test
    @DisplayName("sms 전송 테스트")
    void sending_sms_message() throws URISyntaxException, JsonProcessingException {
        SmsResultDto smsResultDto =
        service.sendSms("01075976959");

    }

}