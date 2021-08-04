package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildAddRequest {

    private Date birthday;

    private String name;

    private String school;

    @JsonProperty("special_note")
    private String specialNote;

}
