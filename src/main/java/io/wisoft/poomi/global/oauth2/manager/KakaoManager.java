package io.wisoft.poomi.global.oauth2.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.global.dto.response.oauth.kakao.KakaoTokenResponse;
import io.wisoft.poomi.global.dto.response.oauth.kakao.KakaoUserInfoResponse;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.oauth2.properties.oauth2.KakaoProperty;
import io.wisoft.poomi.global.oauth2.properties.oauth2.OAuth2Property;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoManager extends OAuth2Manager {


    public KakaoManager(
            @Qualifier("kakao-io.wisoft.poomi.global.oauth2.properties.oauth2.KakaoProperty")
            final OAuth2Property oAuth2Property) {
        super(oAuth2Property);
    }

    @Override
    protected MultiValueMap<String, String> getParams(String code) {
        KakaoProperty kakaoProperty = (KakaoProperty) oAuth2Property;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperty.getClientId());
        params.add("redirect_uri", kakaoProperty.getRedirectionURI());
        params.add("code", code);
        params.add("client_secret", kakaoProperty.getClientSecret());

        return params;
    }

    @Override
    protected String getAccessToken(String tokenResponse) throws JsonProcessingException {
        KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(tokenResponse, KakaoTokenResponse.class);

        return kakaoTokenResponse.getAccessToken();
    }

    @Override
    protected OAuthUserPropertiesResponse stringToUserProperties(String userInfo) throws JsonProcessingException {
        KakaoUserInfoResponse userInfoResponse = objectMapper.readValue(userInfo, KakaoUserInfoResponse.class);

        return OAuthUserPropertiesResponse
                .of(userInfoResponse.getProperties().getNickname(), userInfoResponse.getKakaoAccount().getEmail());
    }
}
