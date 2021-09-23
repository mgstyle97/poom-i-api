package io.wisoft.poomi.configures.property;

import io.wisoft.poomi.global.properties.oauth2.GoogleProperty;
import io.wisoft.poomi.global.properties.oauth2.KakaoProperty;
import io.wisoft.poomi.global.properties.oauth2.NaverProperty;
import io.wisoft.poomi.global.properties.sms.NCSProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(
    {
        NaverProperty.class, GoogleProperty.class,
        KakaoProperty.class, NCSProperty.class
    }
)
@Configuration
public class PropertyConfig {
}
