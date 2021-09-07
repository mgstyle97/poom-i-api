package io.wisoft.poomi.bind.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.Date;

@Getter
@Setter
public class CMInfoRegisterRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NonNull()
    private Date date;
    private String experience;
    private String greeting;

}
