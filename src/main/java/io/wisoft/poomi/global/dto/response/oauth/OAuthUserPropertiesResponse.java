package io.wisoft.poomi.global.dto.response.oauth;

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

    public static OAuthUserPropertiesResponse of(final KakaoUserInfoResponse kakaoUserInfoResponse) {
        String username = kakaoUserInfoResponse.getProperties().getNickname();
        String email = kakaoUserInfoResponse.getKakaoAccount().getEmail();

        return new OAuthUserPropertiesResponse(username, email);
    }
}
