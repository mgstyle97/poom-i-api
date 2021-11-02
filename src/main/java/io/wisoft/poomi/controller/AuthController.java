package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.global.dto.request.auth.SigninRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.SignInResultResponse;
import io.wisoft.poomi.global.dto.response.member.SigninResponse;
import io.wisoft.poomi.global.utils.CookieUtils;
import io.wisoft.poomi.service.auth.AuthService;
import io.wisoft.poomi.service.auth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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



    private ApiResponse<SigninResponse> response(final SignInResultResponse result) {
        return ApiResponse.succeedWithAccessToken(
                HttpStatus.OK,
                result.getSigninResponse(),
                result.getJwtToken()
        );
    }

}
