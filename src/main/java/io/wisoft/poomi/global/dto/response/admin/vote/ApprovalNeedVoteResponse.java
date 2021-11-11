package io.wisoft.poomi.global.dto.response.admin.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalNeedVoteResponse {

    @JsonProperty("vote_id")
    private Long voteId;

    @JsonProperty("purpose_using")
    private String purposeUsing;

    @JsonProperty("registrant_id")
    private Long registrantId;

    @JsonProperty("registrant_nick")
    private String registrantNick;

    @JsonProperty("registrant_address_info")
    private AddressDetailResponse registrantAddressInfo;

    @JsonProperty("vote_address_info")
    private AddressDetailResponse voteAddressInfo;

    @Builder
    public ApprovalNeedVoteResponse(final Long voteId, final String purposeUsing,
                                    final Long registrantId, final String registrantNick,
                                    final AddressDetailResponse registrantAddressInfo,
                                    final AddressDetailResponse voteAddressInfo) {
        this.voteId = voteId;
        this.purposeUsing = purposeUsing;
        this.registrantId = registrantId;
        this.registrantNick = registrantNick;
        this.registrantAddressInfo = registrantAddressInfo;
        this.voteAddressInfo = voteAddressInfo;
    }

    public static ApprovalNeedVoteResponse of(final PlaygroundVote vote) {
        return ApprovalNeedVoteResponse.builder()
                .voteId(vote.getId())
                .purposeUsing(vote.getPurposeUsing())
                .registrantId(vote.getRegistrant().getId())
                .registrantNick(vote.getRegistrant().getNick())
                .registrantAddressInfo(AddressDetailResponse.of(vote.getRegistrant().getAddress()))
                .voteAddressInfo(AddressDetailResponse.of(vote.getAddress()))
                .build();
    }

}
