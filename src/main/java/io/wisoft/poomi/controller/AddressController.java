package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.request.member.AddressRegisterRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.member.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PutMapping("/member/address")
    public ApiResponse<?> address(
            @RequestBody @Valid final AddressRegisterRequest addressRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.OK, addressService.registerAddress(member, addressRegisterRequest));
    }

}
