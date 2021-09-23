package io.wisoft.poomi.global.dto.response.childminder.urgent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildminderUrgentRegisterResponse {

    @JsonProperty("childminder_urgent_id")
    private Long childminderUrgentId;

    private String writer;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildminderUrgentRegisterResponse(final Long childminderUrgentId,
                                              final String writer) {
        this.childminderUrgentId = childminderUrgentId;
        this.writer = writer;
        this.registeredAt = new Date();
    }

    public static ChildminderUrgentRegisterResponse of(final ChildminderUrgent childminderUrgent) {
        return ChildminderUrgentRegisterResponse.builder()
                .childminderUrgentId(childminderUrgent.getId())
                .writer(childminderUrgent.getWriter().getNick())
                .build();
    }

}