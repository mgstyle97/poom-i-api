package io.wisoft.poomi.global.utils;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final JwtTokenProvider jwtTokenProvider;

    public void generateRefreshTokenCookiesAndSave(final String refreshToken, final HttpServletResponse response) {
        Cookie refreshTokenCookie = createTokenCookie(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshToken);

        response.addCookie(refreshTokenCookie);
    }

    private Cookie createTokenCookie(final String cookieName, final String token) {
        Cookie tokenCookie = new Cookie(cookieName, token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge((int) jwtTokenProvider.getExpirationDateFromToken(token).getTime());
        tokenCookie.setPath("/");

        return tokenCookie;
    }

    public Cookie getCookie(final HttpServletRequest request, final String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }

        return null;
    }

}
