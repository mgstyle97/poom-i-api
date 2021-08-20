package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
@Setter
public class SigninRequest {

    @JsonProperty("email")
    private String email;

    private String password;

    public Authentication toAuthentication(final String authority) {
        return new UsernamePasswordAuthenticationToken(
                this.email, this.password, List.of(new SimpleGrantedAuthority(authority))
        );
    }

}
