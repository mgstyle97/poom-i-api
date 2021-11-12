package io.wisoft.poomi.service.admin;

import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.auth.residence.ResidenceCertificationRepository;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.global.dto.request.admin.ApproveResidenceMemberRequest;
import io.wisoft.poomi.global.dto.request.admin.ApproveSignupMemberRequest;
import io.wisoft.poomi.global.dto.response.admin.member.ApprovalNeedMemberResponse;
import io.wisoft.poomi.global.dto.response.admin.vote.ApprovalNeedVoteResponse;
import io.wisoft.poomi.global.exception.exceptions.NotApprovedResidenceMemberException;
import io.wisoft.poomi.service.auth.certification.CertificationService;
import io.wisoft.poomi.service.child_care.playground.PlaygroundVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final ResidenceCertificationRepository residenceCertificationRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final CertificationService certificationService;
    private final PlaygroundVoteService playgroundVoteService;

    @Transactional(readOnly = true)
    public List<ApprovalNeedMemberResponse> lookupApprovalNeedMember() {
        List<Member> memberList = memberRepository.findAll();

        return memberList.stream()
                .filter(member -> member
                        .getResidenceCertification()
                        .getApprovalStatus()
                        .equals(ApprovalStatus.UN_APPROVED))
                .map(ApprovalNeedMemberResponse::of)
                .collect(Collectors.toList());
    }

    public List<ApprovalNeedVoteResponse> lookupApprovalNeedVote() {
        List<PlaygroundVote> playgroundVoteList = playgroundVoteService.findAllPlaygroundVote();

        return playgroundVoteList.stream()
                .filter(vote -> vote.getApprovalStatus().equals(ApprovalStatus.UN_APPROVED))
                .map(ApprovalNeedVoteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveSignupAccount(final ApproveSignupMemberRequest approveSignupMemberRequest) {
        Member member = memberRepository.getMemberByEmail(approveSignupMemberRequest.getAccount());
        validateMemberResidenceApprove(member);

        member.approveSignup(authorityRepository.getUserAuthority());
        memberRepository.save(member);
        certificationService.sendMailOfSignupApproved(member.getEmail());
    }

    @Transactional
    public void approveResidence(final ApproveResidenceMemberRequest approveResidenceMemberRequest) {
        Member member = memberRepository.getMemberByEmail(approveResidenceMemberRequest.getAccount());

        member.approveResidence(jwtTokenProvider.generateResidenceExpiredToken());
        residenceCertificationRepository.save(member.getResidenceCertification());
    }

    public void approvePlaygroundVote(final Long voteId) {
        playgroundVoteService.approvePlaygroundVote(voteId);
    }

    private void validateMemberResidenceApprove(final Member member) {
        if (member.getResidenceCertification().getApprovalStatus().equals(ApprovalStatus.UN_APPROVED)) {
            throw new NotApprovedResidenceMemberException();
        }
    }

}
