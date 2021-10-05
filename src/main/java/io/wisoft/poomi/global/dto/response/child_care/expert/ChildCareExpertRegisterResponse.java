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

    @JsonProperty("childminder_urgent_id")
    private Long childminderUrgentId;

    private String writer;

    @JsonProperty("registered_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registeredAt;

    @Builder
    private ChildCareExpertRegisterResponse(final Long childminderUrgentId,
                                            final String writer) {
        this.childminderUrgentId = childminderUrgentId;
        this.writer = writer;
        this.registeredAt = new Date();
    }

    public static ChildCareExpertRegisterResponse of(final ChildCareExpert childCareExpert) {
        return ChildCareExpertRegisterResponse.builder()
                .childminderUrgentId(childCareExpert.getId())
                .writer(childCareExpert.getWriter().getNick())
                .build();
    }

}
