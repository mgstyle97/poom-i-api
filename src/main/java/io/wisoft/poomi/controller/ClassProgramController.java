package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.*;
import io.wisoft.poomi.bind.request.ClassProgramModifiedRequest;
import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.ClassProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
            @ModelAttribute @Valid final ClassProgramRegisterRequest classProgramRegisterRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @SignInMember final Member member) {
        return ApiResponse
                .succeed(
                        HttpStatus.CREATED,
                        classProgramService.registerClassProgram(member, classProgramRegisterRequest, images)
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

    @GetMapping("/{id}")
    public ApiResponse<ClassProgramSinglePageDto> callClassProgramSinglePage(
            @PathVariable("id") @Valid final Long classId,
            final HttpServletRequest request) {
        final String domainInfo = request
                                    .getRequestURL()
                                    .toString()
                                    .replace(request.getRequestURI(), "");
        return ApiResponse.succeed(
                HttpStatus.OK,
                classProgramService.callClassProgramSinglePage(classId, domainInfo)
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
