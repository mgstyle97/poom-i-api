package io.wisoft.poomi.global.dto.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserResultResponse {

    @JsonProperty("user_properties")
    private OAuthUserPropertiesResponse userProperties;

    @JsonProperty("access_token")
    private String accessToken;



}
