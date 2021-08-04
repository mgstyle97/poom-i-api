package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.request.AddressRegisterRequest;
import io.wisoft.poomi.service.AddressService;
import lombok.RequiredArgsConstructor;
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

    @PutMapping("/member/address")
    public ApiResponse<?> address(@RequestBody @Valid AddressRegisterRequest addressRegisterRequest,
                                  HttpServletRequest request) {
        return ApiResponse.succeed(addressService.registerAddress(request, addressRegisterRequest));
    }

}
