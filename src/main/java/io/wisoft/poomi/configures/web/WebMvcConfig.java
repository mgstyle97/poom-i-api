package io.wisoft.poomi.configures.web;

import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.configures.web.formatter.StringToSocialConverter;
import io.wisoft.poomi.configures.web.resolver.SignInMemberHandlerMethodArgumentResolver;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
@EnableJpaAuditing
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;



    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public SignInMemberHandlerMethodArgumentResolver handlerMethodArgumentResolver() {
        return new SignInMemberHandlerMethodArgumentResolver(jwtTokenProvider, memberRepository);
    }

    @Bean
    public StringToSocialConverter converter() {
        return new StringToSocialConverter();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8080);
        return connector;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(converter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(handlerMethodArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
