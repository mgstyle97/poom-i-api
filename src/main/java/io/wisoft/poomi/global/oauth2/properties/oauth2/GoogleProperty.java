package io.wisoft.poomi.global.oauth2.properties.oauth2;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("google")
public class GoogleProperty extends OAuth2Property {

    private final String redirectionURI;

    public GoogleProperty(final String clientId, final String clientSecret,
                         final String tokenURI, final String userinfoURI,
                         final String redirectionURI) {
        super(clientId, clientSecret, tokenURI, userinfoURI);
        this.redirectionURI = redirectionURI;
    }

}
