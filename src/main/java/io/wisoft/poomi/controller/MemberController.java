package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.MyPageDto;
import io.wisoft.poomi.bind.dto.SigninDto;
import io.wisoft.poomi.bind.dto.CMInfoRegisterDto;
import io.wisoft.poomi.bind.dto.SignupDto;
import io.wisoft.poomi.bind.request.SigninRequest;
import io.wisoft.poomi.bind.request.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signin")
    public ApiResponse<SigninDto> signin(
            @RequestBody @Valid final SigninRequest signinRequest) {
        return ApiResponse.succeed(HttpStatus.OK, memberService.signin(signinRequest));
    }

    @PostMapping("/signup")
    public ApiResponse<SignupDto> signup(
            @ModelAttribute final SignupRequest signupRequest,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images) {
        return ApiResponse.succeed(HttpStatus.CREATED, memberService.signup(signupRequest, images));
    }

    @GetMapping("/signin/oauth2")
    public ResponseEntity<?> sigininOAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("location", "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=12514042527-pm3r9q9804rtlfumt8ino77hmcpsjhcu.apps.googleusercontent.com&scope=profile%20email&state=OQ7BLLoFYEWswnp9YoJuBn6fCAdwUARK9pwD9HR_7yU%3D&redirect_uri=http://localhost:8081/login/oauth2/code/google");


        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);

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

}
