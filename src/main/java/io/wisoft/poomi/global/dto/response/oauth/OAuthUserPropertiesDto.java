package io.wisoft.poomi.global.dto.response.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserPropertiesDto {

    private String username;

    private String email;

    public OAuthUserPropertiesDto(final String username, final String email) {
        this.username = username;
        this.email = email;
    }

    public static OAuthUserPropertiesDto of(final KakaoUserInfoDto kakaoUserInfoDto) {
        String username = kakaoUserInfoDto.getProperties().getNickname();
        String email = kakaoUserInfoDto.getKakaoAccount().getEmail();

        return new OAuthUserPropertiesDto(username, email);
    }
}
