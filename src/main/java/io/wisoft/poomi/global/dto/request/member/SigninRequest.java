package io.wisoft.poomi.global.dto.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class SigninRequest {

    @JsonProperty("email")
    @NotBlank(message = "로그인할 이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "로그인할 비밀번호를 입력해주세요.")
    private String password;

    public Authentication toAuthentication(final String authority) {
        return new UsernamePasswordAuthenticationToken(
                this.email, this.password, List.of(new SimpleGrantedAuthority(authority))
        );
    }

}
