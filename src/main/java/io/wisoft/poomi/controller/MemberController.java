package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.*;
import io.wisoft.poomi.bind.request.SigninRequest;
import io.wisoft.poomi.bind.request.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.MemberService;
import io.wisoft.poomi.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final OAuthService oAuthService;

    @PostMapping("/signin")
    public ApiResponse<SigninDto> signin(
            @RequestBody @Valid final SigninRequest signinRequest) {
        return ApiResponse.succeed(HttpStatus.OK, memberService.signin(signinRequest));
    }

    @PostMapping("/signup")
    public ApiResponse<SignupDto> signup(
            @RequestPart("data") @Valid final SignupRequest signupRequest,
            @RequestPart(value = "images", required = false) @Valid final List<MultipartFile> images) {
        return ApiResponse.succeed(HttpStatus.CREATED, memberService.signup(signupRequest, images));
    }

    @GetMapping("/member/me")
    public ApiResponse<MyPageDto> myPage(@SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.OK, MyPageDto.of(member));
    }

    @PostMapping("/member/childminder-info")
    public ApiResponse<CMInfoRegisterDto> cmInfoRegist(
            @RequestBody @Valid final CMInfoRegisterRequest cmInfoRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.CREATED, memberService.cmInfoUpdate(member, cmInfoRegisterRequest));
    }

    @GetMapping("/oauth2/success")
    public ApiResponse<SigninDto> oauthSignin(final HttpServletRequest request) {
        SigninDto dto = (SigninDto) request.getAttribute("signin-dto");

        return ApiResponse.succeed(HttpStatus.OK, dto);
    }

    @GetMapping("/oauth2")
    public ApiResponse<OAuthUserProperties> oauthCodeToUserInfo(@RequestParam("code") String code) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                oAuthService.getUserProperties(code)
        );
    }

}
