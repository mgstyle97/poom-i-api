package io.wisoft.poomi.service.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.global.dto.response.oauth.KakaoTokenResponseDto;
import io.wisoft.poomi.global.dto.response.oauth.KakaoUserInfoDto;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesDto;
import io.wisoft.poomi.configures.property.properties.oauth2.KakaoProperty;
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

    private final KakaoProperty kakaoProperty;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuthUserPropertiesDto getUserProperties(final String code) {

        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoProperty.getClientId());
            params.add("redirect_uri", kakaoProperty.getRedirectionURI());
            params.add("code", code);
            params.add("client_secret", kakaoProperty.getClientSecret());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoProperty.getTokenURI(),
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            KakaoTokenResponseDto kakaoTokenResponse = objectMapper
                    .readValue(response.getBody(), KakaoTokenResponseDto.class);

            headers.clear();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoTokenResponse.getAccessToken());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

            HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                    kakaoProperty.getUserinfoURI(),
                    HttpMethod.POST,
                    kakaoProfileRequest,
                    String.class
            );
            KakaoUserInfoDto kakaoUserInfoDto = objectMapper.readValue(userInfoResponse.getBody(), KakaoUserInfoDto.class);

            return OAuthUserPropertiesDto.of(kakaoUserInfoDto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            throw e;
        }

    }

}
