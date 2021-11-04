package io.wisoft.poomi.global.dto.response.child_care.expert.apply;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildCareExpertApplyRegisterResponse {

    @JsonProperty("apply_id")
    private Long applyId;

    @JsonProperty("writer_id")
    private Long writerId;

    @Builder
    public ChildCareExpertApplyRegisterResponse(final Long applyId, final Long writerId) {
        this.applyId = applyId;
        this.writerId = writerId;
    }

    public static ChildCareExpertApplyRegisterResponse of(final ChildCareExpertApply expertApply) {
        return ChildCareExpertApplyRegisterResponse.builder()
                .applyId(expertApply.getId())
                .writerId(expertApply.getWriter().getId())
                .build();
    }

}
