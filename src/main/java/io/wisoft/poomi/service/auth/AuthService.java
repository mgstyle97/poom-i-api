package io.wisoft.poomi.service.auth;

import io.wisoft.poomi.configures.security.jwt.JWTToken;
import io.wisoft.poomi.configures.security.jwt.JWTTokenProvider;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.global.dto.request.member.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(readOnly = true)
    public JWTToken signin(final SigninRequest signinRequest) {
        Member member = memberRepository.getMemberByEmail(signinRequest.getEmail());
        Authentication authentication = verifyRequestProperty(signinRequest.toAuthentication(member.getAuthorities()));

        JWTToken tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    private Authentication verifyRequestProperty(final Authentication authentication) {
        return authenticationManagerBuilder
                .getObject()
                .authenticate(authentication);
    }

}
