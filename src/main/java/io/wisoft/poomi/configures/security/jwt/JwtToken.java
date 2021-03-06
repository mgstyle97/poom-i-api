package io.wisoft.poomi.configures.security.jwt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JwtToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("access_token_expiration")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accessTokenExpiration;

    @JsonProperty("refresh_token_expiration")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshTokenExpiration;

    @Builder
    public JwtToken(final String accessToken, final String refreshToken,
                    final Date accessTokenExpiration, final Date refreshTokenExpiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

}
