package io.wisoft.poomi.domain.member.controller;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.service.MemberService;
import io.wisoft.poomi.domain.member.dto.MemberJoinDto;
import io.wisoft.poomi.domain.member.dto.MemberModifiedDto;
import io.wisoft.poomi.response.ApiResult;
import io.wisoft.poomi.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResult<MemberResponse> join(@RequestBody @Valid final MemberJoinDto memberJoinDto) {
        Member newMember = memberService.join(memberJoinDto);

        log.info("new member : {}", newMember);

        return ApiResult.get(HttpStatus.CREATED, MemberResponse.of(newMember), "Created Success");
    }

    @PutMapping("/modified")
    public ApiResult<MemberResponse> modified(@RequestBody @Valid final MemberModifiedDto memberModifiedDto) {
        Member updatedMember = memberService.modified(memberModifiedDto);

        log.info("update member : {}", updatedMember);

        return ApiResult.get(HttpStatus.OK, MemberResponse.of(updatedMember), "Modified Success");
    }

}
