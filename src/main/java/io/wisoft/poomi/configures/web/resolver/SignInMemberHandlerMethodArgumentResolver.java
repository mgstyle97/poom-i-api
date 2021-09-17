package io.wisoft.poomi.configures.web.resolver;

import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class SignInMemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final boolean isSignInMemberAnnotation = parameter.hasParameterAnnotation(SignInMember.class);
        final boolean isMemberParameter = parameter.getParameterType() == Member.class;
        return isSignInMemberAnnotation && isMemberParameter;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = jwtTokenProvider.resolveToken(webRequest.getNativeRequest(HttpServletRequest.class));

        String email = jwtTokenProvider.getUsernameFromToken(token);

        Member member = memberRepository.getMemberByEmail(email);

        return member;
    }
}
