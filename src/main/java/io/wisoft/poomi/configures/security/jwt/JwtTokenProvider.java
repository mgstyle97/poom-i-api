package io.wisoft.poomi.configures.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.wisoft.poomi.global.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    public static final String ACCESS_TOKEN_NAME = "access_token";
    public static final String REFRESH_TOKEN_NAME = "refresh_token";

    private static final String AUTHORITIES_KEY = "auth";

    // 토근 유효시간
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    private static final long CERTIFICATION_TOKEN_EXPIRE_TIME = 3 * 60 * 1000L;

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // jwt 토큰 생성
    @Transactional
    public JwtToken generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        Date refreshTokenExpiration = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        final String refreshToken = generateToken(
                refreshTokenExpiration, null,
                "refresh", "refresh token"
        );

        Date accessTokenExpiration = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        final String accessToken = generateToken(
                accessTokenExpiration, authentication.getName(),
                AUTHORITIES_KEY, authorities
        );

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(getExpirationDateFromToken(accessToken))
                .refreshTokenExpiration(getExpirationDateFromToken(refreshToken))
                .build();
    }

    public String generateExpiredValidationToken() {
        long now = (new Date()).getTime();

        return generateToken(
                new Date(now + CERTIFICATION_TOKEN_EXPIRE_TIME), null,
                "certification", "certification token"
        );
    }

    public String generateToken(final Date expiration, final String subject,
                                final String claimKey, final Object claimValue) {
        return Jwts.builder()
                .setExpiration(expiration)
                .setSubject(subject)
                .claim(claimKey, claimValue)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaimsFromToken(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(
                                claims.get(AUTHORITIES_KEY).toString().split(",")
                        ).map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(getUsernameFromToken(token), "", authorities);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        if (token == null) {
            log.error("권한 정보가 없습니다.");
            return false;
        }
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Wrong JWT Sign");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.error("Illegal JWT Token");
        }

        return false;

    }

}