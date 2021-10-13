package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildCareExpertRegisterResponse {

    @JsonProperty("expert_id")
    private Long expertId;

    private String writer;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    @Builder
    private ChildCareExpertRegisterResponse(final Long expertId,
                                            final String writer) {
        this.expertId = expertId;
        this.writer = writer;
        this.requestedAt = new Date();
    }

    public static ChildCareExpertRegisterResponse of(final ChildCareExpert childCareExpert) {
        return ChildCareExpertRegisterResponse.builder()
                .expertId(childCareExpert.getId())
                .writer(childCareExpert.getWriter().getNick())
                .build();
    }

}
