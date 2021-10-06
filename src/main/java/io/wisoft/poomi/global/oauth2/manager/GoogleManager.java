package io.wisoft.poomi.global.oauth2.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.dto.response.oauth.google.GoogleTokenResponse;
import io.wisoft.poomi.global.dto.response.oauth.google.GoogleUserInfoResponse;
import io.wisoft.poomi.global.oauth2.properties.oauth2.GoogleProperty;
import io.wisoft.poomi.global.oauth2.properties.oauth2.OAuth2Property;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
@Component
public class GoogleManager extends OAuth2Manager {

    private final GoogleProperty googleProperty;

    @Override
    protected MultiValueMap<String, String> getParams(String code) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleProperty.getClientId());
        params.add("redirect_uri", googleProperty.getRedirectionURI());
        params.add("code", code);
        params.add("client_secret", googleProperty.getClientSecret());

        return params;
    }

    @Override
    protected OAuth2Property getOAuth2Property() {
        return this.googleProperty;
    }

    @Override
    protected String getAccessToken(String tokenResponse) throws JsonProcessingException {
        GoogleTokenResponse googleTokenResponse = objectMapper.readValue(tokenResponse, GoogleTokenResponse.class);

        return googleTokenResponse.getAccessToken();
    }

    @Override
    protected OAuthUserPropertiesResponse stringToUserProperties(String userInfo) throws JsonProcessingException {
        GoogleUserInfoResponse userInfoResponse = objectMapper.readValue(userInfo, GoogleUserInfoResponse.class);

        saveProfileImage(
                userInfoResponse.getEmail(),
                userInfoResponse.getPicture()
        );

        return OAuthUserPropertiesResponse.of(userInfoResponse.getName(), userInfoResponse.getEmail());
    }

}
