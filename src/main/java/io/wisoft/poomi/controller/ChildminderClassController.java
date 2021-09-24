package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.childminder.classes.*;
import io.wisoft.poomi.global.dto.request.childminder.classes.ChildminderClassModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.classes.ChildminderClassRegisterRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.childminder.classes.ChildminderClassService;
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
public class ChildminderClassController {

    private final ChildminderClassService childminderClassService;

    @GetMapping
    public ApiResponse<List<ChildminderClassLookupResponse>> lookupAllChildminderClass(@SignInMember final Member member) {
        return ApiResponse
                .succeed(
                        HttpStatus.OK,
                        childminderClassService.findByAddressTag(member.getAddressTag())
                );
    }

    @PostMapping
    public ApiResponse<ChildminderClassRegisterResponse> registerChildminderClass(
            @RequestPart("data") @Valid final ChildminderClassRegisterRequest childminderClassRegisterRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @SignInMember final Member member,
            final HttpServletRequest request) {
        return ApiResponse
                .succeed(
                        HttpStatus.CREATED,
                        childminderClassService
                                .registerChildminderClass
                                        (member, childminderClassRegisterRequest, images, getDomainInfo(request))
                );
    }
    @PatchMapping("/{class-id}")
    public ApiResponse<ChildminderClassModifiedResponse> modifiedChildminderClass(
            @PathVariable("class-id") @Valid final Long classId,
            @RequestBody @Valid final ChildminderClassModifiedRequest childminderClassModifiedRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                        HttpStatus.OK,
                        childminderClassService.modifiedChildminderClass(classId, member, childminderClassModifiedRequest)
        );
    }

    @GetMapping("/{class-id}")
    public ApiResponse<ChildminderClassSinglePageResponse> lookupChildminderClass(
            @PathVariable("class-id") @Valid final Long classId,
            @SignInMember final Member member,
            final HttpServletRequest request) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childminderClassService.lookupChildminderClass(classId, member, getDomainInfo(request))
        );
    }

    @DeleteMapping("/{class-id}")
    public ApiResponse<ChildminderClassDeleteResponse> removeChildminderClass(
            @PathVariable("class-id") @Valid final Long classId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childminderClassService.removeChildminderClass(classId, member)
        );
    }

    @PostMapping("/{class-id}/apply")
    public void applyChildminderClass(@PathVariable("class-id") final Long classId, @SignInMember final Member member) {
        childminderClassService.applyChildminderClass(classId, member);
    }

    @PostMapping("/{class-id}/like")
    public void likeChildminderClass(@PathVariable("class-id") final Long classId, @SignInMember final Member member) {
        childminderClassService.likeChildminderClass(classId, member);
    }

    private String getDomainInfo(final HttpServletRequest request) {
        return request
                .getRequestURL()
                .toString()
                .replace(request.getRequestURI(), "");
    }
}
