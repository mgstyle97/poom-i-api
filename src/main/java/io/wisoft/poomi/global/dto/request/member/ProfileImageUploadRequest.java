package io.wisoft.poomi.global.dto.request.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileImageUploadRequest {

    @JsonProperty("profile-image")
    private String imageMetaData;

}
