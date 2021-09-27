package io.wisoft.poomi.global.oauth2.properties.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
public class OAuth2Property {

    private final String clientId;
    private final String clientSecret;
    private final String tokenURI;
    private final String userinfoURI;

}
