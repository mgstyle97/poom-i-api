package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @Value("${slack.token}")
    private String slackToken;

    @Autowired
    private MemberService memberService;

    @GetMapping("/hello")
    public String hello(@SignInMember final Member member) {
        System.out.println(member.getId());
        return "hello";
    }

}
