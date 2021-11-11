package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.request.admin.ApproveResidenceMemberRequest;
import io.wisoft.poomi.global.dto.request.admin.ApproveSignupMemberRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.admin.member.ApprovalNeedMemberResponse;
import io.wisoft.poomi.global.dto.response.admin.vote.ApprovalNeedVoteResponse;
import io.wisoft.poomi.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/member")
    public ApiResponse<List<ApprovalNeedMemberResponse>> lookupApprovalNeedMember() {
        return ApiResponse.succeed(
                HttpStatus.OK,
                adminService.lookupApprovalNeedMember()
        );
    }

    @GetMapping("/vote")
    public ApiResponse<List<ApprovalNeedVoteResponse>> lookupApprovalNeedVote() {
        return ApiResponse.succeed(
                HttpStatus.OK,
                adminService.lookupApprovalNeedVote()
        );
    }

    @PatchMapping("/approve/signup")
    public void approveSignupMember(@RequestBody @Valid final ApproveSignupMemberRequest approveSignupMemberRequest) {
        adminService.approveSignupAccount(approveSignupMemberRequest);
    }

    @PatchMapping("approve/residence")
    public void approveResidence(
            @RequestBody @Valid final ApproveResidenceMemberRequest approveResidenceMemberRequest) {
        adminService.approveResidence(approveResidenceMemberRequest);
    }

    @PatchMapping("/approve/vote")
    public void approvePlaygroundVote(@RequestParam("vote_id") @Valid final Long voteId) {
        adminService.approvePlaygroundVote(voteId);
    }

}
