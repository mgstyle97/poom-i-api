package io.wisoft.poomi.global.dto.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserResultResponse {

    @JsonProperty("user_properties")
    private OAuthUserPropertiesResponse userProperties;

    @JsonProperty("access_token")
    private String accessToken;

    @Builder
    public OAuthUserResultResponse(final OAuthUserPropertiesResponse userProperties,
                                   final String accessToken) {
        this.userProperties = userProperties;
        this.accessToken = accessToken;
    }

}
