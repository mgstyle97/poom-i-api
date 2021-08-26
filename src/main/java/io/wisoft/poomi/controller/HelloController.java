package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/hello")
    public String hello(@SignInMember final Member member) {
        System.out.println(member.getId());
        return "hello";
    }

}
