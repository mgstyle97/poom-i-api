package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.ClassProgramLookupDto;
import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.ClassProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/class")
public class ClassProgramController {

    private final ClassProgramService classProgramService;

    @GetMapping
    public ApiResponse<List<ClassProgramLookupDto>> allClassProgram(@SignInMember final Member member) {
        return ApiResponse
                .succeed(
                        HttpStatus.OK,
                        classProgramService.findByAddressTag(member.getAddressTag())
                );
    }

    @PostMapping
    public ApiResponse<?> registerClassProgram(
            @RequestBody @Valid final ClassProgramRegisterRequest classProgramRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse
                .succeed(
                        HttpStatus.CREATED,
                        classProgramService.registerClassProgram(member, classProgramRegisterRequest)
                );
    }

    @GetMapping("/{id}/apply")
    public void applyClassProgram(@PathVariable("id") final Long id, @SignInMember final Member member) {
        classProgramService.applyClassProgram(id, member);
    }

}
