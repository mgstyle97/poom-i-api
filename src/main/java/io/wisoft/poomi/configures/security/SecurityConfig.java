package io.wisoft.poomi.configures.security;

import io.wisoft.poomi.configures.security.jwt.JwtSecurityConfig;
import io.wisoft.poomi.configures.security.jwt.JwtAccessDeniedHandler;
import io.wisoft.poomi.configures.security.jwt.JwtAuthenticationEntryPoint;
import io.wisoft.poomi.service.member.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtSecurityConfig jwtSecurityConfig;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private MemberDetailsService detailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        return roleHierarchy;
    }

    @Bean
    public AccessDecisionVoter<?> voter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and().csrf().disable()
                .headers().frameOptions().disable()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(permitAllPatterns()).permitAll()
                .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()

                .and()
                .apply(jwtSecurityConfig);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(detailsService)
            .passwordEncoder(passwordEncoder());
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .requestMatchers(
//                        PathRequest.toH2Console()
//                );
//    }

    private String[] permitAllPatterns() {
        return new String[] {
                "/image/**", "/api/vote/**", "/api/signup", "/api/signin/**", "/favicon.ico/**",
                "/api/sms-certification/**", "/api/mail-certification/**", "/api/download/**",
                "/api/oauth2/**", "/api/image/**", "/api/profile-image/**", "/hello"
        };
    }

}
