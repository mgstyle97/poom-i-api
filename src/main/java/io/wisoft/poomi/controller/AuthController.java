package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.global.dto.request.member.SigninRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
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

    private final CookieUtils cookieUtils;
    private final AuthService authService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/signin")
    public ApiResponse<SigninResponse> signin(
            @RequestBody @Valid final SigninRequest signinRequest, final HttpServletResponse response) {

        final JwtToken jwtToken = authService.signin(signinRequest);
        cookieUtils.generateCookiesAndSave(jwtToken, response);

        return ApiResponse
                .succeed(HttpStatus.OK, null);
    }

}
