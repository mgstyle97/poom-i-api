package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.auth.SigninRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.SignInResultResponse;
import io.wisoft.poomi.global.dto.response.member.SigninResponse;
import io.wisoft.poomi.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ApiResponse<?> signin(
            @RequestBody @Valid final SigninRequest signinRequest) {
        return response(authService.signin(signinRequest));
    }

    @GetMapping("/logout")
    public void logout(@SignInMember final Member member) {
        log.info("Logout member {}", member.getEmail());
    }

    private ApiResponse<SigninResponse> response(final SignInResultResponse result) {
        return ApiResponse.succeedWithAccessToken(
                HttpStatus.OK,
                result.getSigninResponse(),
                result.getJwtToken()
        );
    }

}
