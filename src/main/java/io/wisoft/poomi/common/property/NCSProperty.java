package io.wisoft.poomi.common.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

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
