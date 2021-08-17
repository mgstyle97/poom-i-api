package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.ChildAddDto;
import io.wisoft.poomi.bind.dto.DeleteChildDto;
import io.wisoft.poomi.bind.request.ChildAddRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.ChildService;
import io.wisoft.poomi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class ChildController {

    private final MemberService memberService;
    private final ChildService childService;

    @PostMapping("/child")
    public ApiResponse<ChildAddDto> addChildren(@RequestBody @Valid List<ChildAddRequest> childAddRequest,
                                                HttpServletRequest request) {
        Member member = memberService.generateMemberThroughRequest(request);

        return ApiResponse.succeed(HttpStatus.CREATED, childService.addChildren(member, childAddRequest));
    }

    @DeleteMapping("/child/{id}")
    public ApiResponse<DeleteChildDto> deleteChild(@PathVariable Long id, HttpServletRequest request) {
        Member member = memberService.generateMemberThroughRequest(request);

        return ApiResponse.succeed(HttpStatus.OK, childService.deleteChild(id, member));
    }

}
