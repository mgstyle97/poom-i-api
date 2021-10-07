package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.configures.security.jwt.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final JWTTokenProvider jwtTokenProvider;

    public Cookie createTokenCookie(final String cookieName, final String token) {
        Cookie tokenCookie = new Cookie(cookieName, token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge((int) jwtTokenProvider.getExpirationDateFromToken(token).getTime());
        tokenCookie.setPath("/");

        return tokenCookie;
    }

    public Cookie getCookie(final HttpServletRequest request, final String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }

        return null;
    }

}
