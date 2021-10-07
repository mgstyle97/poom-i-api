package io.wisoft.poomi.global.dto.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.configures.security.jwt.JwtToken;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserResultResponse {

    @JsonProperty("user_properties")
    private OAuthUserPropertiesResponse userProperties;

    @JsonProperty("access_token")
    private JwtToken tokenInfo;

    @Builder
    public OAuthUserResultResponse(final OAuthUserPropertiesResponse userProperties,
                                   final JwtToken tokenInfo) {
        this.userProperties = userProperties;
        this.tokenInfo = tokenInfo;
    }

}
