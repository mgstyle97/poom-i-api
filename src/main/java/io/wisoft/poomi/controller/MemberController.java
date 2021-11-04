package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.configures.web.formatter.Social;
import io.wisoft.poomi.configures.web.validator.pdf.SignUpFile;
import io.wisoft.poomi.global.dto.request.member.ProfileImageUploadRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.*;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserResultResponse;
import io.wisoft.poomi.service.member.MemberService;
import io.wisoft.poomi.service.auth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(
            @RequestBody @Valid final SignupRequest signupRequest) {
        return ApiResponse
                .succeed(HttpStatus.CREATED, memberService.signup(signupRequest)
                );
    }

    @GetMapping("/member/me")
    public ApiResponse<MyPageResponse> myPage(@SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.OK, MyPageResponse.of(member));
    }

    @GetMapping("/member/poomi")
    public ApiResponse<ChildAndPoomiResponse> childAndPoomi(@SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                memberService.childAndPoomi(member)
        );
    }

    @PostMapping("/member/profile-image")
    public void registerProfileImage(
            @RequestBody @Valid final ProfileImageUploadRequest profileImage,
            @SignInMember final Member member) {
        memberService.saveProfileImage(profileImage, member);
    }

    @GetMapping("/oauth2/success")
    public ApiResponse<SigninResponse> oauthSignin(final HttpServletRequest request) {
        SigninResponse dto = (SigninResponse) request.getAttribute("signin-dto");

        return ApiResponse.succeed(HttpStatus.OK, dto);
    }

    @GetMapping("/oauth2/{social}")
    public ApiResponse<OAuthUserPropertiesResponse> oauthCodeToUserInfo(
            @PathVariable("social") @Valid final Social social,
            @RequestParam("code") @Valid final String code) {

        final OAuthUserResultResponse userResultResponse = oAuth2Service.getUserProperties(social, code);

        return generateApiResponseHasAccessToken(userResultResponse);
    }

    private ApiResponse<OAuthUserPropertiesResponse> generateApiResponseHasAccessToken(
            final OAuthUserResultResponse userResultResponse) {
        if (!ObjectUtils.isEmpty(userResultResponse.getTokenInfo())) {
            final JwtToken tokenInfo = userResultResponse.getTokenInfo();
            userResultResponse.setTokenInfo(null);

            return ApiResponse.succeedWithAccessToken(
                    HttpStatus.OK,
                    userResultResponse.getUserProperties(),
                    tokenInfo
            );
        }

        return ApiResponse.succeed(
                HttpStatus.OK,
                userResultResponse.getUserProperties()
        );
    }

}
