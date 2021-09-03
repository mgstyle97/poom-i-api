package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassProgramRegisterRequest {

    private String title;

    private String contents;

    private Long capacity;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;


}
