package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.playground.ChildCarePlaygroundRegisterRequest;
import io.wisoft.poomi.global.dto.request.child_care.playground.ResidenceCertificationRegisterRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.ChildCarePlaygroundLookupResponse;
import io.wisoft.poomi.service.child_care.playground.ChildCarePlaygroundService;
import io.wisoft.poomi.service.child_care.playground.PlaygroundVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/playground")
public class ChildCarePlaygroundController {

    private final ChildCarePlaygroundService childCarePlaygroundService;

    @GetMapping
    public ApiResponse<List<ChildCarePlaygroundLookupResponse>> lookupPlaygroundList(
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childCarePlaygroundService.lookupPlaygroundList(member)
        );
    }

    @PostMapping
    public void registerPlayground(
            @RequestBody @Valid final ChildCarePlaygroundRegisterRequest registerRequest,
            @SignInMember final Member member) {
        childCarePlaygroundService.registerPlayground(registerRequest, member);
    }

}
