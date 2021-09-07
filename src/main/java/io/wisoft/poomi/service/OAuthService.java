package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.bind.dto.KakaoTokenResponseDto;
import io.wisoft.poomi.bind.dto.KakaoUserInfoDto;
import io.wisoft.poomi.bind.dto.OAuthUserProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USERINFO_URL;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuthUserProperties getUserProperties(final String code) {

        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("redirect_uri", "http://localhost:3000/signup/oauth/kakao");
            params.add("code", code);
            params.add("client_secret", KAKAO_CLIENT_SECRET);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_TOKEN_URI,
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
                    KAKAO_USERINFO_URL,
                    HttpMethod.POST,
                    kakaoProfileRequest,
                    String.class
            );
            KakaoUserInfoDto kakaoUserInfoDto = objectMapper.readValue(userInfoResponse.getBody(), KakaoUserInfoDto.class);

            return OAuthUserProperties.of(kakaoUserInfoDto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

}
