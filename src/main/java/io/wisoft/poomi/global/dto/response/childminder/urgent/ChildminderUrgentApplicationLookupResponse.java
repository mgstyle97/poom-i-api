package io.wisoft.poomi.global.dto.response.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.urgent.application.ChildminderUrgentApplication;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChildminderUrgentApplicationLookupResponse {

    private String applier;

    @JsonProperty("childminder_score")
    private Integer childminderScore;

    private String contents;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildminderUrgentApplicationLookupResponse(final ChildminderUrgentApplication application) {
        this.applier = application.getWriter().getNick();
        this.childminderScore = application.getWriter().getScore();
        this.contents = application.getContents();
        this.registeredAt = new Date();
    }

    public static ChildminderUrgentApplicationLookupResponse of(final ChildminderUrgentApplication application) {
        return ChildminderUrgentApplicationLookupResponse.builder()
                .application(application)
                .build();
    }

}
