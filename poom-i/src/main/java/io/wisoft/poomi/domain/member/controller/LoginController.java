package io.wisoft.poomi.domain.member.controller;

import io.wisoft.poomi.domain.member.dto.AuthInfo;
import io.wisoft.poomi.domain.member.dto.LoginDto;
import io.wisoft.poomi.domain.member.service.AuthService;
import io.wisoft.poomi.exception.InvalidApproachException;
import io.wisoft.poomi.response.ApiResult;
import io.wisoft.poomi.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResult<AuthInfo> login(@RequestBody @Valid final LoginDto loginDto,
                                           HttpSession session,
                                           HttpServletResponse response) {
        AuthInfo authInfo = authService.authInfo(loginDto);
        session.setAttribute("USER_INFO", authInfo);

        Cookie userCookie = new Cookie("USER_EMAIL", authInfo.getEmail());
        userCookie.setPath("/");
        userCookie.setMaxAge(3600);
        response.addCookie(userCookie);

        return ApiResult.get(HttpStatus.OK, authInfo, "Success Login");
    }

    @GetMapping("/logout")
    public ApiResult<?> logout(final HttpSession session) {
        if (session.isNew()) {
            session.invalidate();
            throw new InvalidApproachException();
        }
        session.invalidate();
        return ApiResult.get(HttpStatus.OK, null, "Success Logout");
    }

}
