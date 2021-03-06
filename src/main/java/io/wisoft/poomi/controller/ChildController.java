package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.member.child.ChildSimpleDataResponse;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.child.ChildAddResponse;
import io.wisoft.poomi.global.dto.response.member.child.ChildDeleteResponse;
import io.wisoft.poomi.global.dto.request.member.ChildAddRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.member.ChildService;
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

    @GetMapping
    public ApiResponse<List<ChildSimpleDataResponse>> getChildSimpleList(@SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                childService.childSimpleList(member)
        );
    }

    @PostMapping
    public ApiResponse<List<ChildAddResponse>> addChildren(
            @RequestBody @Valid final List<ChildAddRequest> childAddRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.CREATED, childService.updateChildren(member, childAddRequest));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ChildDeleteResponse> deleteChild(
            @PathVariable final Long id,
            @SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.OK, childService.deleteChild(id, member));
    }

}
