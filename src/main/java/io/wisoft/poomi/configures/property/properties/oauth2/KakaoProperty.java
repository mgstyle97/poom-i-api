package io.wisoft.poomi.configures.property.properties.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("kakao")
public class KakaoProperty {

    private final String clientId;
    private final String clientSecret;
    private final String redirectionURI;
    private final String tokenURI;
    private final String userinfoURI;

}
