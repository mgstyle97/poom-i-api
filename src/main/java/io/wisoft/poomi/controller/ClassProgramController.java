package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.service.ClassProgramService;
import io.wisoft.poomi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/class")
public class ClassProgramController {

    private final MemberService memberService;
    private final ClassProgramService classProgramService;

    @GetMapping
    public ApiResponse<?> allClassProgram(final HttpServletRequest request) {
        Member member = memberService.generateMemberThroughRequest(request);

        return ApiResponse.succeed(HttpStatus.OK, classProgramService.findByAddressTag(member.getAddressTag()));
    }

}
