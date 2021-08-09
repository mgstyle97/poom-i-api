package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.SigninDto;
import io.wisoft.poomi.bind.dto.CMInfoRegisterDto;
import io.wisoft.poomi.bind.dto.SignupDto;
import io.wisoft.poomi.bind.request.SigninRequest;
import io.wisoft.poomi.bind.request.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signin")
    public ApiResponse<SigninDto> signin(@RequestBody @Valid SigninRequest signinRequest) {
        return ApiResponse.succeed(HttpStatus.OK, memberService.signin(signinRequest));
    }

    @PostMapping("/signup")
    public ApiResponse<SignupDto> signup(
            @ModelAttribute SignupRequest signupRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> files) {
        return ApiResponse.succeed(HttpStatus.CREATED, memberService.signup(signupRequest, files));
    }

    @PostMapping("/member/childminder-info")
    public ApiResponse<CMInfoRegisterDto> cmInfoRegist(
            @RequestBody @Valid CMInfoRegisterRequest cmInfoRegisterRequest,
            HttpServletRequest request) {
        return ApiResponse.succeed(HttpStatus.CREATED, memberService.cmInfoRegist(request, cmInfoRegisterRequest));
    }

    @GetMapping("/oauth2/success")
    public ApiResponse<SigninDto> oauthSignin(HttpServletRequest request) {
        SigninDto dto = (SigninDto) request.getAttribute("signin-dto");

        return ApiResponse.succeed(HttpStatus.OK, dto);
    }

}
