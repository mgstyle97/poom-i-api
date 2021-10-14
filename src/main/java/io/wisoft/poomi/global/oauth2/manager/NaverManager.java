package io.wisoft.poomi.global.oauth2.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.dto.response.oauth.naver.NaverTokenResponse;
import io.wisoft.poomi.global.dto.response.oauth.naver.NaverUserInfoResponse;
import io.wisoft.poomi.global.oauth2.properties.oauth2.NaverProperty;
import io.wisoft.poomi.global.oauth2.properties.oauth2.OAuth2Property;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
@Component
public class NaverManager extends OAuth2Manager {

    private final NaverProperty naverProperty;

    @Override
    protected MultiValueMap<String, String> getParams(String code) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverProperty.getClientId());
        params.add("client_secret", naverProperty.getClientSecret());
        params.add("code", code);

        return params;
    }

    @Override
    protected OAuth2Property getOAuth2Property() {
        return this.naverProperty;
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
