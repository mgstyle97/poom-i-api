package io.wisoft.poomi.global.oauth2.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.dto.response.oauth.naver.NaverTokenResponse;
import io.wisoft.poomi.global.dto.response.oauth.naver.NaverUserInfoResponse;
import io.wisoft.poomi.global.oauth2.properties.oauth2.NaverProperty;
import io.wisoft.poomi.global.oauth2.properties.oauth2.OAuth2Property;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class NaverManager extends OAuth2Manager {

    public NaverManager(
            @Qualifier("naver-io.wisoft.poomi.global.oauth2.properties.oauth2.NaverProperty")
            final OAuth2Property oAuth2Property
    ) {
        super(oAuth2Property);
    }

    @Override
    protected MultiValueMap<String, String> getParams(String code) {

        final NaverProperty naverProperty = (NaverProperty) oAuth2Property;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverProperty.getClientId());
        params.add("client_secret", naverProperty.getClientSecret());
        params.add("code", code);

        return params;
    }

    @Override
    protected String getAccessToken(String tokenResponse) throws JsonProcessingException {
        NaverTokenResponse naverTokenResponse = objectMapper.readValue(tokenResponse, NaverTokenResponse.class);

        return naverTokenResponse.getAccessToken();
    }

    @Override
    protected OAuthUserPropertiesResponse stringToUserProperties(String userInfo) throws JsonProcessingException {
        NaverUserInfoResponse userInfoResponse = objectMapper.readValue(userInfo, NaverUserInfoResponse.class);

        return OAuthUserPropertiesResponse
                .of(userInfoResponse.getResponse().getName(), userInfoResponse.getResponse().getEmail());
    }
}
