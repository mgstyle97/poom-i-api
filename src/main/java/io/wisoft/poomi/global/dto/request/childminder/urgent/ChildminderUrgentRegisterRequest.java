package io.wisoft.poomi.global.dto.request.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChildminderUrgentRegisterRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @JsonProperty("is_recruit")
    private Boolean isRecruit;

    @JsonProperty("child_id")
    private Long childId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("end_time")
    private LocalDateTime endTime;

}