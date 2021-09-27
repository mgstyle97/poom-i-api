package io.wisoft.poomi.global.dto.response.oauth;

import io.wisoft.poomi.global.dto.response.oauth.kakao.KakaoUserInfoResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserPropertiesResponse {

    private String username;

    private String email;

    public OAuthUserPropertiesResponse(final String username, final String email) {
        this.username = username;
        this.email = email;
    }

    public static OAuthUserPropertiesResponse of(final String name, final String email) {
        return new OAuthUserPropertiesResponse(name, email);
    }
}
