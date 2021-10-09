package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.global.dto.request.member.SigninRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.SigninResponse;
import io.wisoft.poomi.global.utils.CookieUtils;
import io.wisoft.poomi.global.utils.SessionUtils;
import io.wisoft.poomi.service.auth.AuthService;
import io.wisoft.poomi.service.auth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final SessionUtils sessionUtils;
    private final CookieUtils cookieUtils;
    private final AuthService authService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/signin")
    public ApiResponse<SigninResponse> signin(
            @RequestBody @Valid final SigninRequest signinRequest, final HttpServletRequest request) {

        final JwtToken jwtToken = authService.signin(signinRequest);
        sessionUtils.setSessionToken(request, jwtToken.getAccessToken(), "access_token");
        sessionUtils.setSessionToken(request, jwtToken.getRefreshToken(), "refresh_token");

        return ApiResponse
                .succeed(HttpStatus.OK, null);
    }

}
