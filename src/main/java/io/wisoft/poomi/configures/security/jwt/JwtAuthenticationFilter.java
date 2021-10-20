package io.wisoft.poomi.configures.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.global.utils.CookieUtils;
import io.wisoft.poomi.global.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CookieUtils cookieUtils;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = jwtTokenProvider.resolveToken(request);
        try {
            if (validateJwtToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            Cookie refreshTokenCookie = cookieUtils.getCookie(request, JwtTokenProvider.REFRESH_TOKEN_NAME);
            verifyRefreshToken(refreshTokenCookie.getValue(), accessToken, response);
        }

        filterChain.doFilter(request, response);

    }

    private boolean validateJwtToken(String jwtToken) {
        return StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken);
    }

    private void verifyRefreshToken(final String refreshToken, final String accessToken,
                                    final HttpServletResponse response) {
        try {
            if (validateJwtToken(refreshToken)) {
                String memberEmail = jwtTokenProvider.getUsernameFromToken(accessToken);

                Member member = memberRepository.getMemberByEmail(memberEmail);
                Authentication authentication = authenticationManagerBuilder
                        .getObject()
                        .authenticate(member.toAuthentication());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
                cookieUtils.generateTokenCookieAndSave(
                        jwtToken.getAccessToken(), JwtTokenProvider.ACCESS_TOKEN_NAME, response
                );
                cookieUtils.generateTokenCookieAndSave(
                        jwtToken.getRefreshToken(), JwtTokenProvider.REFRESH_TOKEN_NAME, response
                );
            }
        } catch (ExpiredJwtException e) {
            log.error("Expired Refresh Token");
        }
    }
}
