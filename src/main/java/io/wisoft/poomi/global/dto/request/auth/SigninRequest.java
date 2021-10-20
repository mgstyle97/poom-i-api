package io.wisoft.poomi.global.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.authority.Authority;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class SigninRequest {

    @JsonProperty("email")
    @Email(message = "로그인할 이메일 형식이 부적절합니다.")
    @NotBlank(message = "로그인할 이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "로그인할 비밀번호를 입력해주세요.")
    private String password;

    public Authentication toAuthentication(final Set<Authority> authority) {
        final List<? extends GrantedAuthority> authorities = authority.stream()
                .map(Authority::getAuthority)
                .map(authorityValue -> new SimpleGrantedAuthority(authorityValue))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                this.email, this.password, authorities
        );
    }

}
