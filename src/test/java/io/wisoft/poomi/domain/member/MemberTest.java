package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.repository.MemberRepository;
import io.wisoft.poomi.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private SignupRequest joinRequest;

    @BeforeEach
    void setup() {
        joinRequest = new SignupRequest();
        joinRequest.setName("migni");
        joinRequest.setPhoneNumber("010-1111-1111");
        joinRequest.setEmail("migni@gmail.com");
        joinRequest.setLoginId("migni");
        joinRequest.setPassword("1234");
        joinRequest.setNick("migni");
    }

    @Test
    @DisplayName("1. 사용자 회원가입 서비스 테스트")
    void signup() {

    }

}
