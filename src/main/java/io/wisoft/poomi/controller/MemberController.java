package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.SigninDto;
import io.wisoft.poomi.bind.dto.CMInfoRegisterDto;
import io.wisoft.poomi.bind.dto.SignupDto;
import io.wisoft.poomi.bind.request.SigninRequest;
import io.wisoft.poomi.bind.request.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signin")
    public ApiResponse<SigninDto> login(@RequestBody @Valid SigninRequest signinRequest) {
        return ApiResponse.succeed(memberService.signin(signinRequest));
    }

    @PostMapping("/signup")
    public ApiResponse<SignupDto> join(
            @ModelAttribute SignupRequest signupRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> files) {
        return ApiResponse.succeed(memberService.signup(signupRequest, files));
    }

    @PostMapping("/member/childminder-info")
    public ApiResponse<CMInfoRegisterDto> cmInfoRegist(
            @RequestBody @Valid CMInfoRegisterRequest cmInfoRegisterRequest) {
        Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();

        return ApiResponse.succeed(memberService.cmInfoRegist(authInfo, cmInfoRegisterRequest));
    }

}
