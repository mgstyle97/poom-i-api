package io.wisoft.poomi.global.dto.request.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChildminderUrgentRegisterRequest {

    @NotBlank(message = "내용을 입력해주세요.")
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