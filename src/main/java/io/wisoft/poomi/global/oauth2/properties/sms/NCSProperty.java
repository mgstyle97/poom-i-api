package io.wisoft.poomi.global.oauth2.properties.sms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("ncs")
public class NCSProperty {

    private final String accessKey;
    private final String secretKey;
    private final String serviceId;
    private final String from;

}
