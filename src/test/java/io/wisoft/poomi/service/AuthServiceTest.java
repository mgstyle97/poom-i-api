package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.service.auth.AuthService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @Disabled
    void sms_test() throws URISyntaxException, JsonProcessingException {
    }

}