package io.wisoft.poomi.global.dto.response.admin.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.auth.residence.ResidenceCertification;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResidenceCertificationInfoResponse {

    private Long id;

    @JsonProperty("certification_status")
    private ApprovalStatus approvalStatus;

    @JsonProperty("residence_file_URL")
    private String residenceFileURL;

    @Builder
    public ResidenceCertificationInfoResponse(final Long id,
                                              final ApprovalStatus approvalStatus,
                                              final String residenceFileURL) {
        this.id = id;
        this.approvalStatus = approvalStatus;
        this.residenceFileURL = residenceFileURL;
    }

    public static ResidenceCertificationInfoResponse of(final ResidenceCertification residenceCertification) {
        return ResidenceCertificationInfoResponse
                .builder()
                .id(residenceCertification.getId())
                .approvalStatus(residenceCertification.getApprovalStatus())
                .residenceFileURL(residenceCertification.getResidenceFile().getFileAccessURI())
                .build();
    }

}
