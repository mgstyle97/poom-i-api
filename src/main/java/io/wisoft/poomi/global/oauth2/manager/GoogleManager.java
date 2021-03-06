package io.wisoft.poomi.global.oauth2.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.dto.response.oauth.google.GoogleTokenResponse;
import io.wisoft.poomi.global.dto.response.oauth.google.GoogleUserInfoResponse;
import io.wisoft.poomi.global.oauth2.properties.oauth2.GoogleProperty;
import io.wisoft.poomi.global.oauth2.properties.oauth2.OAuth2Property;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GoogleManager extends OAuth2Manager {

    public GoogleManager(
            @Qualifier("google-io.wisoft.poomi.global.oauth2.properties.oauth2.GoogleProperty")
            final OAuth2Property oAuth2Property
    ) {
        super(oAuth2Property);
    }

    @Override
    protected MultiValueMap<String, String> getParams(String code) {

        final GoogleProperty googleProperty = (GoogleProperty) oAuth2Property;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleProperty.getClientId());
        params.add("redirect_uri", googleProperty.getRedirectionURI());
        params.add("code", code);
        params.add("client_secret", googleProperty.getClientSecret());

        return params;
    }

    @Override
    protected String getAccessToken(String tokenResponse) throws JsonProcessingException {
        GoogleTokenResponse googleTokenResponse = objectMapper.readValue(tokenResponse, GoogleTokenResponse.class);

        return googleTokenResponse.getAccessToken();
    }

    @Override
    protected OAuthUserPropertiesResponse stringToUserProperties(String userInfo) throws JsonProcessingException {
        GoogleUserInfoResponse userInfoResponse = objectMapper.readValue(userInfo, GoogleUserInfoResponse.class);

        return OAuthUserPropertiesResponse.of(userInfoResponse.getName(), userInfoResponse.getEmail());
    }

}
