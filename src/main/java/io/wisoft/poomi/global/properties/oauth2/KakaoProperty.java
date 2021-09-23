package io.wisoft.poomi.global.properties.oauth2;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("kakao")
public class KakaoProperty extends OAuth2Property {

    private final String redirectionURI;

    public KakaoProperty(final String clientId, final String clientSecret,
                         final String tokenURI, final String userinfoURI,
                         final String redirectionURI) {
        super(clientId, clientSecret, tokenURI, userinfoURI);
        this.redirectionURI = redirectionURI;
    }

}
