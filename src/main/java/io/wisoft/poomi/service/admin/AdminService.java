package io.wisoft.poomi.service.admin;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.global.dto.request.admin.ApproveSignupMemberRequest;
import io.wisoft.poomi.service.auth.certification.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    private CertificationService certificationService;

    public void approveSignupAccount(final ApproveSignupMemberRequest approveSignupMemberRequest) {
        Member member = memberRepository.getMemberByEmail(approveSignupMemberRequest.getAccount());

        member.approveSignup(authorityRepository.getUserAuthority());
        memberRepository.save(member);
        certificationService.sendMailOfSignupApproved(member.getEmail());
    }

}
