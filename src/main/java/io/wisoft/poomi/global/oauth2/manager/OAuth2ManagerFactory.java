package io.wisoft.poomi.global.oauth2.manager;

import io.wisoft.poomi.configures.web.formatter.Social;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2ManagerFactory {

    private final KakaoManager kakaoManager;

    public OAuth2Manager getOAuth2Manager(final Social social) {
        switch (social) {
            case KAKAO:
                return kakaoManager;
            case NAVER:
            case GOOGLE:
            default:
                throw new IllegalArgumentException("존재하지 않는 소셜 타입입니다.");
        }
    }

}
