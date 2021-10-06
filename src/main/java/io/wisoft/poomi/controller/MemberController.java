package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.formatter.Social;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.*;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.dto.request.member.SigninRequest;
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
    public ApiResponse<SigninResponse> signin(
            @RequestBody @Valid final SigninRequest signinRequest) {
        return ApiResponse
                .succeedWithAccessToken(HttpStatus.OK, null, memberService.signin(signinRequest));
    }

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(
            @RequestPart("data") @Valid final SignupRequest signupRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ApiResponse
                .succeed(HttpStatus.CREATED, memberService.signup(signupRequest, images));
    }

    @GetMapping("/member/me")
    public ApiResponse<MyPageResponse> myPage(@SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.OK, MyPageResponse.of(member));
    }

    @PostMapping("/member/profile-image")
    public void registProfileImage(
            @RequestPart("profile") final MultipartFile profileImage,
            @SignInMember final Member member) {

    }

    @GetMapping("/oauth2/success")
    public ApiResponse<SigninResponse> oauthSignin(final HttpServletRequest request) {
        SigninResponse dto = (SigninResponse) request.getAttribute("signin-dto");

        return ApiResponse.succeed(HttpStatus.OK, dto);
    }

    @GetMapping("/oauth2/{social}")
    public ApiResponse<OAuthUserPropertiesResponse> oauthCodeToUserInfo(
            @PathVariable("social")@Valid final Social social,
            @RequestParam("code") @Valid final String code) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                oAuth2Service.getUserProperties(social, code)
        );
    }

}
