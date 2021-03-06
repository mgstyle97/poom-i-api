package io.wisoft.poomi.configures.property;

import io.wisoft.poomi.global.aws.S3Bucket;
import io.wisoft.poomi.global.oauth2.properties.oauth2.GoogleProperty;
import io.wisoft.poomi.global.oauth2.properties.oauth2.KakaoProperty;
import io.wisoft.poomi.global.oauth2.properties.oauth2.NaverProperty;
import io.wisoft.poomi.global.oauth2.properties.sms.NCSProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(
        {
                NaverProperty.class, GoogleProperty.class,
                KakaoProperty.class, NCSProperty.class,
                S3Bucket.class
        }
)
@Configuration
public class PropertyConfig {
}
