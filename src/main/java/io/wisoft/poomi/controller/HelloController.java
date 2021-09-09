package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/hello")
    public String hello(@SignInMember final Member member) {
        System.out.println(member.getId());
        return "hello";
    }

    @GetMapping("/login/oauth2/code/google")
    public void result(HttpServletRequest request) {
        System.out.println("Success");
    }

    @GetMapping("/error-log")
    public void errorLog() {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> request = new HashMap<>();
        request.put("username", "error-bot");
        request.put("text", "test");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request);

        String url = "";

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

}
