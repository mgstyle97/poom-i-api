package io.wisoft.poomi.global.dto.request.childminder.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildminderClassModifiedRequest {

    private String contents;

    private Long capacity;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;

}
