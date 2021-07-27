package io.wisoft.poomi.service;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberDetails;
import io.wisoft.poomi.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository
                .findMemberByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("No member data about login id"));
        return MemberDetails.builder()
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .authorities(member.getAuthorities())
                .build();
    }
}
