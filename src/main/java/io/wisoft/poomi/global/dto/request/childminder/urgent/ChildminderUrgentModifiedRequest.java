package io.wisoft.poomi.global.dto.request.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChildminderUrgentModifiedRequest {

    private String contents;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;

    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

}
