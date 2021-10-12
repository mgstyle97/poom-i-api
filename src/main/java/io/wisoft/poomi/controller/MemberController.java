package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.configures.web.formatter.Social;
import io.wisoft.poomi.configures.web.validator.image.Image;
import io.wisoft.poomi.configures.web.validator.pdf.SignUpFile;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.*;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
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
            @RequestPart("data") @Valid final SignupRequest signupRequest,
            @RequestPart("images") @SignUpFile List<MultipartFile> images) {
        return ApiResponse
                .succeed(HttpStatus.CREATED, memberService.signup(signupRequest, images));
    }

    @GetMapping("/member/me")
    public ApiResponse<MyPageResponse> myPage(@SignInMember final Member member) {
        return ApiResponse.succeed(HttpStatus.OK, MyPageResponse.of(member));
    }

    @PostMapping("/member/profile-image")
    public void registerProfileImage(
            @RequestPart("profile") final MultipartFile profileImage,
            @SignInMember final Member member) {

    }

    @GetMapping("/oauth2/success")
    public ApiResponse<SigninResponse> oauthSignin(final HttpServletRequest request) {
        SigninResponse dto = (SigninResponse) request.getAttribute("signin-dto");

        return ApiResponse.succeed(HttpStatus.OK, dto);
    }

    @GetMapping("/oauth2/{social}")
    public ApiResponse<OAuthUserResultResponse> oauthCodeToUserInfo(
            @PathVariable("social") @Valid final Social social,
            @RequestParam("code") @Valid final String code) {

        final OAuthUserResultResponse userResultResponse = oAuth2Service.getUserProperties(social, code);

        return generateApiResponseHasAccessToken(userResultResponse);
    }

    private ApiResponse<OAuthUserResultResponse> generateApiResponseHasAccessToken(final OAuthUserResultResponse userResultResponse) {
        if (!ObjectUtils.isEmpty(userResultResponse.getTokenInfo())) {
            final JwtToken tokenInfo = userResultResponse.getTokenInfo();
            userResultResponse.setTokenInfo(null);

            return ApiResponse.succeedWithAccessToken(
                    HttpStatus.OK,
                    userResultResponse,
                    tokenInfo
            );
        }

        return ApiResponse.succeed(
                HttpStatus.OK,
                userResultResponse
        );
    }

}
