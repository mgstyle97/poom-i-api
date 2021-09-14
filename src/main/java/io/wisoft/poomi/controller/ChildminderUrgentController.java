package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.childminder.urgent.ChildminderUrgentRegisterDto;
import io.wisoft.poomi.bind.request.childminder.urgent.ChildminderUrgentRegisterRequest;
import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.ChildminderUrgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/urgent")
public class ChildminderUrgentController {

    private final ChildminderUrgentService childminderUrgentService;

    @PostMapping
    public ApiResponse<ChildminderUrgentRegisterDto> registerChildminderUrgent(
            @RequestBody @Valid final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.CREATED,
                childminderUrgentService.registerChildminderUrgent(childminderUrgentRegisterRequest, member)
        );
    }

}
