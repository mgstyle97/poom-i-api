package io.wisoft.poomi.configures.property;

import io.wisoft.poomi.configures.property.properties.oauth2.KakaoProperty;
import io.wisoft.poomi.configures.property.properties.sms.NCSProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(
        {KakaoProperty.class, NCSProperty.class}
)
@Configuration
public class PropertyConfig {
}
