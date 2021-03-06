package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupApplyRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupSimpleDataResponse;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.child_care.group.*;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.child_care.group.ChildCareGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class ChildCareGroupController {

    private final ChildCareGroupService childCareGroupService;

    @GetMapping
    public ApiResponse<List<ChildCareGroupSimpleDataResponse>> groupSimpleList(@SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareGroupService.groupSimpleList(member)
        );
    }

    @GetMapping("/detail")
    public ApiResponse<List<ChildCareGroupLookupResponse>> lookupAllChildCareGroup(@SignInMember final Member member) {
        return ApiResponse
                .succeed(
                        HttpStatus.OK,
                        childCareGroupService.findByAddressTag(member.getAddressTag())
                );
    }

    @PostMapping
    public ApiResponse<ChildCareGroupRegisterResponse> registerChildCareGroup(
            @RequestBody @Valid final ChildCareGroupRegisterRequest childCareGroupRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse
                .succeed(
                        HttpStatus.CREATED,
                        childCareGroupService.registerChildCareGroup(member, childCareGroupRegisterRequest)
                );
    }

    @PatchMapping("/{group-id}")
    public ApiResponse<ChildCareGroupModifiedResponse> modifiedChildCareGroup(
            @PathVariable("group-id") @Valid final Long groupId,
            @RequestBody @Valid final ChildCareGroupModifiedRequest childCareGroupModifiedRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareGroupService.modifiedChildCareGroup(groupId, member, childCareGroupModifiedRequest)
        );
    }

    @GetMapping("/{group-id}")
    public ApiResponse<ChildCareGroupLookupResponse> lookupChildCareGroup(
            @PathVariable("group-id") @Valid final Long groupId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareGroupService.lookupChildCareGroup(groupId, member)
        );
    }

    @DeleteMapping("/{group-id}")
    public ApiResponse<ChildCareGroupDeleteResponse> removeChildCareGroup(
            @PathVariable("group-id") @Valid final Long groupId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCareGroupService.removeChildCareGroup(groupId, member)
        );
    }

    @DeleteMapping("/{group-id}/withdraw")
    public void withdrawGroup(
            @PathVariable("group-id") @Valid final Long groupId,
            @SignInMember final Member member) {
        childCareGroupService.withdrawGroup(groupId, member);
    }

    @PostMapping("/{group-id}/apply")
    public void applyChildCareGroup(
            @PathVariable("group-id") final Long groupId,
            @RequestBody @Valid final ChildCareGroupApplyRequest applyRequest,
            @SignInMember final Member member) {
        childCareGroupService.applyChildCareGroup(groupId, member, applyRequest);
    }

    @PostMapping("/{group-id}/approve/{apply-id}")
    public void approveGroupApply(
            @PathVariable("group-id") @Valid final Long boardId,
            @PathVariable("apply-id") @Valid final Long applyId,
            @SignInMember final Member member) {
        childCareGroupService.approveGroupApply(boardId, member, applyId);
    }

}
