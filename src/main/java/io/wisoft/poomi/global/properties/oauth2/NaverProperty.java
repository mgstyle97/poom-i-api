package io.wisoft.poomi.global.properties.oauth2;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("naver")
public class NaverProperty extends OAuth2Property {

    public NaverProperty(final String clientId, final String clientSecret,
                         final String tokenURI, final String userinfoURI) {
        super(clientId, clientSecret, tokenURI, userinfoURI);
    }

}
