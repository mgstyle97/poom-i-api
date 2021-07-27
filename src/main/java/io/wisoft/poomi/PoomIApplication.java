package io.wisoft.poomi;

import io.wisoft.poomi.common.property.NCSProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(
        NCSProperty.class
)
public class PoomIApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoomIApplication.class, args);
    }

}
