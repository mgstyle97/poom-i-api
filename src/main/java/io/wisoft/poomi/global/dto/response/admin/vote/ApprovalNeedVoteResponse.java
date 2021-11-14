package io.wisoft.poomi.global.dto.response.admin.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.response.admin.member.ResidenceInfoResponse;
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

    @JsonProperty("residence_info")
    private ResidenceInfoResponse residenceInfo;

    @JsonProperty("vote_address_info")
    private AddressDetailResponse voteAddressInfo;

    @Builder
    public ApprovalNeedVoteResponse(final Long voteId, final String purposeUsing,
                                    final Long registrantId, final String registrantNick,
                                    final ResidenceInfoResponse residenceInfo,
                                    final AddressDetailResponse voteAddressInfo) {
        this.voteId = voteId;
        this.purposeUsing = purposeUsing;
        this.registrantId = registrantId;
        this.registrantNick = registrantNick;
        this.residenceInfo = residenceInfo;
        this.voteAddressInfo = voteAddressInfo;
    }

    public static ApprovalNeedVoteResponse of(final PlaygroundVote vote, final Member registrant) {
        return ApprovalNeedVoteResponse.builder()
                .voteId(vote.getId())
                .purposeUsing(vote.getPurposeUsing())
                .registrantId(vote.getRegistrant().getId())
                .registrantNick(vote.getRegistrant().getNick())
                .residenceInfo(
                        ResidenceInfoResponse.of(registrant.getAddress(), registrant.getResidenceCertification())
                )
                .voteAddressInfo(AddressDetailResponse.of(vote.getAddress()))
                .build();
    }

}
