package io.wisoft.poomi.global.dto.response.child_care.expert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChildCareExpertApplyLookupResponse {

    private String applier;

    @JsonProperty("writer_score")
    private Integer writerScore;

    private String contents;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    @Builder
    private ChildCareExpertApplyLookupResponse(final ChildCareExpertApply application) {
        this.applier = application.getWriter().getNick();
        this.writerScore = application.getWriter().getScore();
        this.contents = application.getContents();
        this.requestedAt = new Date();
    }

    public static ChildCareExpertApplyLookupResponse of(final ChildCareExpertApply application) {
        return ChildCareExpertApplyLookupResponse.builder()
                .application(application)
                .build();
    }

}
