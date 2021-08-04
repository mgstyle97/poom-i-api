package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Getter
@Setter
public class SigninRequest {

    @JsonProperty("login_id")
    private String loginId;

    private String password;

    public Authentication toAuthentication() {
        return new UsernamePasswordAuthenticationToken(this.loginId, this.getPassword());
    }

}
