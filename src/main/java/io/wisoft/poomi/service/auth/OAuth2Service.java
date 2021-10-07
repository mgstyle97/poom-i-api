package io.wisoft.poomi.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.configures.web.formatter.Social;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserPropertiesResponse;
import io.wisoft.poomi.global.dto.response.oauth.OAuthUserResultResponse;
import io.wisoft.poomi.global.oauth2.manager.OAuth2Manager;
import io.wisoft.poomi.global.oauth2.manager.OAuth2ManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class OAuth2Service {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final OAuth2ManagerFactory oAuth2ManagerFactory;

    public OAuthUserResultResponse getUserProperties(final Social social, final String code) {

        OAuth2Manager oAuth2Manager = oAuth2ManagerFactory.getOAuth2Manager(social);

        OAuthUserPropertiesResponse userPropertiesResponse = getOAuth2UserProperties(code, oAuth2Manager);
        OAuthUserResultResponse userResultResponse = OAuthUserResultResponse.builder()
                .userProperties(userPropertiesResponse)
                .build();

        Optional<Member> optionalMember = memberRepository.findByEmail(userPropertiesResponse.getEmail());
        optionalMember.ifPresent(member -> {
            final JwtToken tokenInfo = jwtTokenProvider.generateToken(member.toAuthentication());
            userResultResponse.setTokenInfo(tokenInfo);
        });

        return userResultResponse;
    }

    private OAuthUserPropertiesResponse getOAuth2UserProperties(final String code, final OAuth2Manager oAuth2Manager) {
        try {
            return oAuth2Manager.getOAuthUserProperties(code);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Json parsing error");
        }
    }

}
