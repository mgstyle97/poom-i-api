package io.wisoft.poomi.configures.security;

import io.wisoft.poomi.configures.security.jwt.JWTToken;
import io.wisoft.poomi.global.dto.response.member.SigninResponse;
import io.wisoft.poomi.configures.security.jwt.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        Authentication usernamePasswordToken = toUsernamePasswordToken(oAuth2User);
        JWTToken jwtToken = jwtTokenProvider.generateToken(usernamePasswordToken);

        request.setAttribute("signin-dto", SigninResponse.of(usernamePasswordToken.getName()));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/api/oauth2/success");
        dispatcher.forward(request, response);
    }

    private Authentication toUsernamePasswordToken(final OAuth2User oAuth2User) {
        String userEmail = getOAuth2UserEmail(oAuth2User.getAttributes());
        return new UsernamePasswordAuthenticationToken(userEmail, null, oAuth2User.getAuthorities());
    }

    private String getOAuth2UserEmail(final Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        if (email == null) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            return (String) kakaoAccount.get("email");
        }
        return email;
    }

}
