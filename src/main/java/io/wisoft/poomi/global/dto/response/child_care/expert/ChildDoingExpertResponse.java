package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChildDoingExpertResponse {

    @JsonProperty("expert_id")
    private Long expertId;

    @JsonProperty("manager_id")
    private Long managerId;

    @JsonProperty("manager_nick")
    private String managerNick;

    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Builder
    public ChildDoingExpertResponse(final Long expertId, final Long managerId,
                                    final String managerNick, final LocalDateTime endTime) {
        this.expertId = expertId;
        this.managerId = managerId;
        this.managerNick = managerNick;
        this.endTime = endTime;
    }

    public static ChildDoingExpertResponse of(final ChildCareExpert expert) {
        return ChildDoingExpertResponse.builder()
                .expertId(expert.getId())
                .managerId(expert.getManager().getId())
                .managerNick(expert.getManager().getNick())
                .endTime(expert.getEndTime())
                .build();
    }

}
