package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.request.AddressRegisterRequest;
import io.wisoft.poomi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/member/address")
    public ApiResponse<?> address(@RequestBody @Valid AddressRegisterRequest addressRegisterRequest) {
        Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();

        return ApiResponse.succeed(addressService.registerAddress(authInfo, addressRegisterRequest));
    }

}
