package io.wisoft.poomi.bind.request.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChildminderUrgentRegisterRequest {

    private String contents;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("end_time")
    private LocalDateTime endTime;

}