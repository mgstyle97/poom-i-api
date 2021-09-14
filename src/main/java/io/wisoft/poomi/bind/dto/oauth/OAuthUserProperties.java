package io.wisoft.poomi.bind.dto.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserProperties {

    private String username;

    private String email;

    public OAuthUserProperties(final String username, final String email) {
        this.username = username;
        this.email = email;
    }

    public static OAuthUserProperties of(final KakaoUserInfoDto kakaoUserInfoDto) {
        String username = kakaoUserInfoDto.getProperties().getNickname();
        String email = kakaoUserInfoDto.getKakaoAccount().getEmail();

        return new OAuthUserProperties(username, email);
    }
}
