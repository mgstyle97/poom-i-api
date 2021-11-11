package io.wisoft.poomi.global.dto.response.admin.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.address.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDetailResponse {

    private Long id;

    @JsonProperty("post_code")
    private String postCode;

    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @JsonProperty("extra_address")
    private String extraAddress;

    @Builder
    public AddressDetailResponse(final Long id,
                                 final String postCode, final String address,
                                 final String detailAddress, final String extraAddress) {
        this.id = id;
        this.postCode = postCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }

    public static AddressDetailResponse of(final Address address) {
        return AddressDetailResponse.builder()
                .id(address.getId())
                .postCode(address.getPostCode())
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .extraAddress(address.getAddressTag().getExtraAddress())
                .build();
    }

}
