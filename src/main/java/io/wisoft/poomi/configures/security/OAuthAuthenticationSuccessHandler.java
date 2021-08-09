package io.wisoft.poomi.configures.security;

import io.wisoft.poomi.bind.dto.SigninDto;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        Authentication usernamePasswordToken = toUsernamePasswordToken(oAuth2User);
        String jwtToken = jwtTokenProvider.generateToken(usernamePasswordToken);

        request.setAttribute("signin-dto", SigninDto.of(usernamePasswordToken.getName(), jwtToken));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/api/oauth2/success");
        dispatcher.forward(request, response);
    }

    private Authentication toUsernamePasswordToken(OAuth2User oAuth2User) {
        String userEmail = (String) oAuth2User.getAttributes().get("email");
        return new UsernamePasswordAuthenticationToken(userEmail, null);
    }

}
