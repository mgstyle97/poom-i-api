package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.member.ChildAddDto;
import io.wisoft.poomi.bind.dto.member.ChildDeleteDto;
import io.wisoft.poomi.bind.request.member.ChildAddRequest;
import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/member/child")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @PostMapping
    public ApiResponse<List<ChildAddDto>> addChildren(
            @RequestBody @Valid final List<ChildAddRequest> childAddRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.CREATED, childService.updateChildren(member, childAddRequest));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ChildDeleteDto> deleteChild(
            @PathVariable final Long id,
            @SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.OK, childService.deleteChild(id, member));
    }

}
