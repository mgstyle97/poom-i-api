package io.wisoft.poomi.global.dto.response.admin.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.auth.residence.ResidenceCertification;
import io.wisoft.poomi.domain.member.address.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResidenceInfoResponse {

    private Long id;

    @JsonProperty("post_code")
    private String postCode;

    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @JsonProperty("extra_address")
    private String extraAddress;

    @JsonProperty("certification_info")
    private ResidenceCertificationInfoResponse certificationInfo;

    @Builder
    public ResidenceInfoResponse(final Long id,
                                 final String postCode, final String address,
                                 final String detailAddress, final String extraAddress,
                                 final ResidenceCertificationInfoResponse certificationInfo) {
        this.id = id;
        this.postCode = postCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.certificationInfo = certificationInfo;
    }

    public static ResidenceInfoResponse of(final Address address,
                                           final ResidenceCertification residenceCertification) {
        return ResidenceInfoResponse.builder()
                .id(address.getId())
                .postCode(address.getPostCode())
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .extraAddress(address.getAddressTag().getExtraAddress())
                .certificationInfo(ResidenceCertificationInfoResponse.of(residenceCertification))
                .build();
    }

}
