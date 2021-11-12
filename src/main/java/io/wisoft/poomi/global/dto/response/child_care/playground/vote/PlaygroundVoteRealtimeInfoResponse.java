package io.wisoft.poomi.global.dto.response.child_care.playground.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.VoteType;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PlaygroundVoteRealtimeInfoResponse {

    @JsonProperty("vote_id")
    private Long voteId;

    private String registrant;

    @JsonProperty("expired_statue")
    private String expiredStatus;

    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @JsonProperty("voting_rate")
    private String votingRate;

    @JsonProperty("agree_rate")
    private String agreeRate;

    @JsonProperty("disagree_rate")
    private String disagreeRate;

    @JsonProperty("voting_yet_list")
    Map<String, List<String>> votingYetList;

    @Builder
    public PlaygroundVoteRealtimeInfoResponse(final Long voteId,
                                              final String registrant, final String address,
                                              final String detailAddress, final String expiredStatus,
                                              final String votingRate, final String agreeRate,
                                              final String disagreeRate,
                                              final Map<String, List<String>> votingYetList) {
        this.voteId = voteId;
        this.registrant = registrant;
        this.address = address;
        this.detailAddress = detailAddress;
        this.expiredStatus = expiredStatus;
        this.votingRate = votingRate;
        this.agreeRate = agreeRate;
        this.disagreeRate = disagreeRate;
        this.votingYetList = votingYetList;
    }

    public static PlaygroundVoteRealtimeInfoResponse of(final PlaygroundVote vote) {
        return generateVoteRealtimeInfo(vote);
    }

    public static PlaygroundVoteRealtimeInfoResponse ofNotVotingList(final PlaygroundVote vote) {
        PlaygroundVoteRealtimeInfoResponse voteRealtimeInfoResponse = generateVoteRealtimeInfo(vote);
        voteRealtimeInfoResponse.setVotingYetList(vote.getNotVotingDongAndHo());

        return voteRealtimeInfoResponse;
    }

    private static PlaygroundVoteRealtimeInfoResponse generateVoteRealtimeInfo(final PlaygroundVote vote) {
        return PlaygroundVoteRealtimeInfoResponse.builder()
                .voteId(vote.getId())
                .registrant(vote.getRegistrant().getNick())
                .address(vote.getAddress().getAddress())
                .detailAddress(vote.getAddress().getDetailAddress())
                .expiredStatus(vote.getExpiredStatus().toString())
                .votingRate(String.format("%.2f", vote.calculateVotingRate()))
                .agreeRate(String.format("%.2f", vote.calculateRateByVoteType(VoteType.AGREE)))
                .disagreeRate(String.format("%.2f", vote.calculateRateByVoteType(VoteType.DISAGREE)))
                .build();
    }

}
