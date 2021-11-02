package io.wisoft.poomi.service.auth;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.global.dto.request.auth.SigninRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.member.SignInResultResponse;
import io.wisoft.poomi.global.dto.response.member.SigninResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(readOnly = true)
    public SignInResultResponse signin(final SigninRequest signinRequest) {
        Member member = memberRepository.getMemberByEmail(signinRequest.getEmail());
        Authentication authentication = verifyRequestProperty(signinRequest.toAuthentication(member.getAuthorities()));

        JwtToken tokenInfo = jwtTokenProvider.generateToken(authentication);

        return SignInResultResponse.of(member, tokenInfo);
    }

    private Authentication verifyRequestProperty(final Authentication authentication) {
        return authenticationManagerBuilder
                .getObject()
                .authenticate(authentication);
    }

}
