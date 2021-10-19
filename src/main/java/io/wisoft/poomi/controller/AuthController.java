package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.global.dto.request.member.SigninRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.SigninResponse;
import io.wisoft.poomi.global.utils.CookieUtils;
import io.wisoft.poomi.service.auth.AuthService;
import io.wisoft.poomi.service.auth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final CookieUtils cookieUtils;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/signin")
    public ApiResponse<?> signin(
            @RequestBody @Valid final SigninRequest signinRequest, final HttpServletResponse response) {


        return ApiResponse.succeedWithAccessToken(
                HttpStatus.OK, null, authService.signin(signinRequest)
        );
    }

}
