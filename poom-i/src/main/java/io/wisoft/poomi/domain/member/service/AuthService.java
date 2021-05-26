package io.wisoft.poomi.domain.member.service;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.dto.AuthInfo;
import io.wisoft.poomi.domain.member.dto.LoginDto;
import io.wisoft.poomi.domain.member.exception.WrongEmailPasswordException;
import io.wisoft.poomi.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthInfo authInfo(final LoginDto loginDto) {
        Member member = memberRepository
                .findByEmail(loginDto.getEmail())
                .orElseThrow(NotFoundException::new);
        if (!Member.checkPassword(member, loginDto.getPassword())) {
            throw new WrongEmailPasswordException();
        }
        member.setRecentLoginAt(LocalDateTime.now());
        memberRepository.save(member);

        return AuthInfo.of(member);
    }

}
