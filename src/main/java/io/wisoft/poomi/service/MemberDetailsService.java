package io.wisoft.poomi.service;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberDetails;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository
                .getMemberByEmail(username);

        return MemberDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(member.getAuthorities())
                .build();
    }
}
