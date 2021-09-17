package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.*;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesDto;
import io.wisoft.poomi.global.dto.request.member.SigninRequest;
import io.wisoft.poomi.global.dto.request.member.CMInfoRegisterRequest;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.member.MemberService;
import io.wisoft.poomi.service.oauth2.OAuth2Service;
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
    private final OAuth2Service oAuth2Service;

    @PostMapping("/signin")
    public ApiResponse<SigninDto> signin(
            @RequestBody @Valid final SigninRequest signinRequest) {
        return ApiResponse.succeed(HttpStatus.OK, memberService.signin(signinRequest));
    }

    @PostMapping("/signup")
    public ApiResponse<SignupDto> signup(
            @RequestPart("data") @Valid final SignupRequest signupRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ApiResponse
                .succeed(HttpStatus.CREATED, memberService.signup(signupRequest, images));
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
    public ApiResponse<OAuthUserPropertiesDto> oauthCodeToUserInfo(@RequestParam("code") String code) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                oAuth2Service.getUserProperties(code)
        );
    }

}
