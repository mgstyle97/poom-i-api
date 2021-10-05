package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertApplyModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertApplyRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertApplyLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertModifiedResponse;
import io.wisoft.poomi.global.dto.response.child_care.expert.ChildCareExpertRegisterResponse;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.expert.ChildCareExpertRegisterRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.child_care.expert.ChildCareExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expert")
public class ChildCareExpertController {

    private final ChildCareExpertService childCareExpertService;

    @GetMapping
    public ApiResponse<List<ChildCareExpertLookupResponse>> lookupAllChildCareExpert(
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareExpertService.lookupAllChildCareExpert(member.getAddressTag())
        );
    }

    @GetMapping("/{expert-id}")
    public ApiResponse<ChildCareExpertLookupResponse> lookupChildCareExpert(
            @PathVariable("expert-id") @Valid final Long expertId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareExpertService.lookupChildCareExpert(expertId, member)
        );
    }

    @PostMapping
    public ApiResponse<ChildCareExpertRegisterResponse> registerChildCareExpert(
            @RequestBody @Valid final ChildCareExpertRegisterRequest childCareExpertRegisterRequest,
            @SignInMember final Member member) {

        return ApiResponse.succeed(
                HttpStatus.CREATED,
                childCareExpertService.registerChildCareExpert(childCareExpertRegisterRequest, member)
        );
    }

    @PatchMapping("/{expert-id}")
    public ApiResponse<ChildCareExpertModifiedResponse> modifiedChildCareExpert(
            @PathVariable("expert-id") @Valid final Long expertId,
            @RequestBody @Valid final ChildCareExpertModifiedRequest childCareExpertModifiedRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareExpertService.modifiedChildCareExpert(
                        expertId, childCareExpertModifiedRequest, member
                )
        );
    }

    @DeleteMapping("/{expert-id}")
    public void removeChildCareExpert(
            @PathVariable("expert-id") @Valid final Long expertId,
            @SignInMember final Member member) {
        childCareExpertService.removeChildCareExpert(expertId, member);
    }

    @PostMapping("/{expert-id}/apply")
    public void applyChildCareExpert(
            @PathVariable("expert-id") @Valid final Long expertId,
            @RequestBody @Valid final ChildCareExpertApplyRequest childCareExpertApplyRequest,
            @SignInMember final Member member) {
        childCareExpertService.applyChildCareExpert(expertId, member, childCareExpertApplyRequest);
    }

    @GetMapping("/{expert-id}/apply")
    public ApiResponse<List<ChildCareExpertApplyLookupResponse>> lookupAllApplications(
            @PathVariable("expert-id") @Valid final Long expertId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareExpertService.lookupAllApplicationChildCareExpert(expertId, member)
        );
    }

    @PatchMapping("/{expert-id}/apply/{apply-id}")
    public void modifiedChildCareExpertApply(
            @PathVariable("expert-id") @Valid final Long expertId,
            @PathVariable("apply-id") @Valid final Long applyId,
            @RequestBody @Valid ChildCareExpertApplyModifiedRequest applyModifiedRequest,
            @SignInMember final Member member) {
        childCareExpertService
                .modifiedChildCareExpertApply(expertId, applyId, member, applyModifiedRequest);
    }

    @DeleteMapping("/{expert-id}/apply/{apply-id}")
    public void removeChildCareExpertApply(
            @PathVariable("expert-id") @Valid final Long expertId,
            @PathVariable("apply-id") @Valid final Long applyId,
            @SignInMember Member member) {
        childCareExpertService.removeChildCareExpertApply(expertId, applyId, member);
    }

    @PostMapping("/{expert-id}/like")
    public void likeChildCareExpert(
            @PathVariable("expert-id") @Valid final Long expertId,
            @SignInMember final Member member) {
        childCareExpertService.likeChildCareExpert(expertId, member);
    }

    @PostMapping("/{expert-id}/approve/{apply-id}")
    public void approveExpertApply(
            @PathVariable("expert-id") @Valid final Long expertId,
            @PathVariable("apply-id") @Valid final Long applyId,
            @SignInMember Member member) {
        childCareExpertService.approveExpertApply(expertId, applyId, member);
    }

}
