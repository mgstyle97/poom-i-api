package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class ChildAddRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String name;

    private String school;

    @JsonProperty("special_note")
    private String specialNote;

}
