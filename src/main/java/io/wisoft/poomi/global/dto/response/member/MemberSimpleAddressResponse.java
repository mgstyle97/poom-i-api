package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSimpleAddressResponse {

    @JsonProperty("member_id")
    private Long memberId;

    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @Builder
    public MemberSimpleAddressResponse(final Long memberId,
                                       final String address, final String detailAddress) {
        this.memberId = memberId;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public static MemberSimpleAddressResponse of(final Member member) {
        return MemberSimpleAddressResponse.builder()
                .memberId(member.getId())
                .address(member.getAddress().getAddress())
                .detailAddress(member.getAddress().getDetailAddress())
                .build();
    }

}
