package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.request.AddressRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.AddressService;
import io.wisoft.poomi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final MemberService memberService;

    @PutMapping("/member/address")
    public ApiResponse<?> address(@RequestBody @Valid AddressRegisterRequest addressRegisterRequest,
                                  HttpServletRequest request) {
        Member member = memberService.generateMemberThroughRequest(request);

        return ApiResponse.succeed(HttpStatus.OK, addressService.registerAddress(member, addressRegisterRequest));
    }

}
