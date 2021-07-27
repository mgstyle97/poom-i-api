package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.LoginDto;
import io.wisoft.poomi.bind.dto.CMInfoRegisterDto;
import io.wisoft.poomi.bind.dto.JoinDto;
import io.wisoft.poomi.bind.request.LoginRequest;
import io.wisoft.poomi.bind.request.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.JoinRequest;
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

    @PostMapping("/login")
    public ApiResponse<LoginDto> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ApiResponse.succeed(memberService.login(loginRequest));
    }

    @PostMapping("/join")
    public ApiResponse<JoinDto> join(
            @ModelAttribute JoinRequest joinRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> files
            ) {
        return ApiResponse.succeed(memberService.join(joinRequest, files));
    }

    @PostMapping("/member/childminder-info")
    public ApiResponse<CMInfoRegisterDto> cmInfoRegist(
            @RequestBody @Valid CMInfoRegisterRequest cmInfoRegisterRequest) {
        Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();

        return ApiResponse.succeed(memberService.cmInfoRegist(authInfo, cmInfoRegisterRequest));
    }

}
