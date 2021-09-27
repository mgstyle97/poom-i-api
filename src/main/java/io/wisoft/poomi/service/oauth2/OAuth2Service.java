package io.wisoft.poomi.service.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.configures.web.formatter.Social;
import io.wisoft.poomi.global.dto.response.oauth.KakaoTokenResponse;
import io.wisoft.poomi.global.dto.response.oauth.KakaoUserInfoResponse;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.oauth2.manager.OAuth2Manager;
import io.wisoft.poomi.global.oauth2.manager.OAuth2ManagerFactory;
import io.wisoft.poomi.global.oauth2.properties.oauth2.KakaoProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class OAuth2Service {

    private final OAuth2ManagerFactory oAuth2ManagerFactory;

    public OAuthUserPropertiesResponse getUserProperties(final Social social, final String code) {

        OAuth2Manager oAuth2Manager = oAuth2ManagerFactory.getOAuth2Manager(social);

        return getOAuth2UserProperties(code, oAuth2Manager);
    }

    private OAuthUserPropertiesResponse getOAuth2UserProperties(final String code, final OAuth2Manager oAuth2Manager) {
        try {
            return oAuth2Manager.getOAuthUserProperties(code);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Json parsing error");
        }
    }

}
