package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.childminder.urgent.ChildminderUrgentLookupResponse;
import io.wisoft.poomi.global.dto.response.childminder.urgent.ChildminderUrgentModifiedResponse;
import io.wisoft.poomi.global.dto.response.childminder.urgent.ChildminderUrgentRegisterResponse;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.urgent.ChildminderUrgentRegisterRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.childminder.urgent.ChildminderUrgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/urgent")
public class ChildminderUrgentController {

    private final ChildminderUrgentService childminderUrgentService;

    @GetMapping
    public ApiResponse<List<ChildminderUrgentLookupResponse>> lookupAllChildminderUrgent(
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childminderUrgentService.lookupAllChildminderUrgent(member)
        );
    }

    @PostMapping
    public ApiResponse<ChildminderUrgentRegisterResponse> registerChildminderUrgent(
            @RequestBody @Valid final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.CREATED,
                childminderUrgentService.registerChildminderUrgent(childminderUrgentRegisterRequest, member)
        );
    }

    @PatchMapping("/{urgent-id}")
    public ApiResponse<ChildminderUrgentModifiedResponse> modifiedChildminderUrgent(
            @PathVariable("urgent-id") @Valid final Long urgentId,
            @RequestBody @Valid final ChildminderUrgentModifiedRequest childminderUrgentModifiedRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childminderUrgentService.modifiedChildminderUrgent(
                        urgentId, childminderUrgentModifiedRequest, member
                )
        );
    }


}
