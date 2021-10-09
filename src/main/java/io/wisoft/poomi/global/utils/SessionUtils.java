package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class SessionUtils {

    private final JwtTokenProvider jwtTokenProvider;

    public void setSessionToken(final HttpServletRequest request, final String token, final String attributeName) {
        HttpSession session = request.getSession();

        session.setAttribute(attributeName, token);

    }

    public String getToken(final HttpServletRequest request, final String attributeName) {
        HttpSession session = request.getSession();

        return (String) session.getAttribute("access_token");
    }

}
