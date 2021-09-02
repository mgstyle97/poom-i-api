package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.ClassProgramDeleteDto;
import io.wisoft.poomi.bind.dto.ClassProgramLookupDto;
import io.wisoft.poomi.bind.dto.ClassProgramModifiedDto;
import io.wisoft.poomi.bind.dto.ClassProgramRegisterDto;
import io.wisoft.poomi.bind.request.ClassProgramModifiedRequest;
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
    public ApiResponse<ClassProgramRegisterDto> registerClassProgram(
            @RequestBody @Valid final ClassProgramRegisterRequest classProgramRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse
                .succeed(
                        HttpStatus.CREATED,
                        classProgramService.registerClassProgram(member, classProgramRegisterRequest)
                );
    }
    @PatchMapping("/{id}")
    public ApiResponse<ClassProgramModifiedDto> modifiedClassProgram(
            @PathVariable("id") @Valid final Long id,
            @RequestBody @Valid final ClassProgramModifiedRequest classProgramModifiedRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                        HttpStatus.OK,
                        classProgramService.modifiedClassProgram(member, id, classProgramModifiedRequest)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ClassProgramDeleteDto> removeClassProgram(
            @PathVariable("id") @Valid final Long id,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                classProgramService.removeClassProgram(member, id)
        );
    }

    @GetMapping("/{id}/apply")
    public void applyClassProgram(@PathVariable("id") final Long id, @SignInMember final Member member) {
        classProgramService.applyClassProgram(id, member);
    }

    @GetMapping("/{id}/like")
    public void likeClassProgram(@PathVariable("id") final Long id, @SignInMember final Member member) {
        classProgramService.likeClassProgram(id, member);
    }

}
