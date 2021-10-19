package io.wisoft.poomi.global.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageToBase64MetaData {

    @JsonProperty("meta_data")
    private String metaData;

}
