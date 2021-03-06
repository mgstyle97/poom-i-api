package io.wisoft.poomi.global.oauth2.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.oauth2.properties.oauth2.OAuth2Property;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Getter
public abstract class OAuth2Manager {

    protected final ObjectMapper objectMapper;
    protected final OAuth2Property oAuth2Property;
    private final RestTemplate restTemplate;

    protected OAuth2Manager(final OAuth2Property oAuth2Property) {
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
        this.oAuth2Property = oAuth2Property;
    }

    public OAuthUserPropertiesResponse getOAuthUserProperties(final String code) throws JsonProcessingException {

        MultiValueMap<String, String> params = getParams(code);

        HttpHeaders headers = getFormUrlencodedHeader();

        HttpEntity<MultiValueMap<String, String>> tokenRequestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> tokenResponse = restTemplate
                .postForEntity(oAuth2Property.getTokenURI(), tokenRequestEntity, String.class);

        String accessToken = getAccessToken(tokenResponse.getBody());

        headers = getAuthorizationHeader(accessToken);

        HttpEntity<HttpHeaders> userPropertiesRequestEntity = new HttpEntity<>(headers);

        String userInfo = restTemplate
                .postForObject(getOAuth2Property().getUserinfoURI(), userPropertiesRequestEntity, String.class);

        return stringToUserProperties(userInfo);
    }

    private HttpHeaders getFormUrlencodedHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        return headers;
    }

    private HttpHeaders getAuthorizationHeader(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        return headers;
    }


    protected abstract MultiValueMap<String, String> getParams(final String code);
    protected abstract String getAccessToken(final String tokenResponse)
            throws JsonProcessingException;
    protected abstract OAuthUserPropertiesResponse stringToUserProperties(final String userInfo)
            throws JsonProcessingException;
}
