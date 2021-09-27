package io.wisoft.poomi.global.dto.response.oauth.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUserInfoResponse {

    private String sub;

    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String picture;

    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    private String locale;

    private String hd;

}
