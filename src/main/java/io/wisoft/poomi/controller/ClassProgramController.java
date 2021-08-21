package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.ClassProgramLookupDto;
import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.service.ClassProgramService;
import io.wisoft.poomi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/class")
public class ClassProgramController {

    private final MemberService memberService;
    private final ClassProgramService classProgramService;

    @GetMapping
    public ApiResponse<List<ClassProgramLookupDto>> allClassProgram(final HttpServletRequest request) {
        Member member = memberService.generateMemberThroughRequest(request);

        return ApiResponse
                .succeed(
                        HttpStatus.OK,
                        classProgramService.findByAddressTag(member.getAddressTag())
                );
    }

    @PostMapping
    public ApiResponse<?> registerClassProgram(
            @RequestBody @Valid final ClassProgramRegisterRequest classProgramRegisterRequest,
            final HttpServletRequest request) {
        Member member = memberService.generateMemberThroughRequest(request);

        return ApiResponse
                .succeed(
                        HttpStatus.CREATED,
                        classProgramService.registerClassProgram(member, classProgramRegisterRequest)
                );
    }

}
