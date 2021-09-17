package io.wisoft.poomi.service.member;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberDetails;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository
                .getMemberByEmail(username);
        log.info("Find member: {}", member.getEmail());

        return MemberDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(member.getAuthorities())
                .build();
    }


}
