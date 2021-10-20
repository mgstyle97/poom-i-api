package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.request.admin.ApproveSignupMemberRequest;
import io.wisoft.poomi.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/approve/signup")
    public void approveSignupMember(@RequestBody @Valid final ApproveSignupMemberRequest approveSignupMemberRequest) {
        adminService.approveSignupAccount(approveSignupMemberRequest);
    }

}
