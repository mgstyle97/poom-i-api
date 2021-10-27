package io.wisoft.poomi.global.dto.response.child_care.playground.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.PlaygroundVoter;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.VoteType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static PlaygroundVoteRealtimeInfoResponse of(final PlaygroundVote playgroundVote) {
        return PlaygroundVoteRealtimeInfoResponse.builder()
                .voteId(playgroundVote.getId())
                .registrant(playgroundVote.getRegistrant().getNick())
                .address(playgroundVote.getAddress().getAddress())
                .detailAddress(playgroundVote.getAddress().getDetailAddress())
                .expiredStatus(playgroundVote.getExpiredStatus().toString())
                .votingRate(String.format("%.2f", playgroundVote.calculateVotingRate()))
                .agreeRate(String.format("%.2f", playgroundVote.calculateAgreeRate()))
                .disagreeRate(String.format("%.2f", playgroundVote.calculateDisagreeRate()))
                .votingYetList(generateVotingYetList(playgroundVote))
                .build();
    }

    private static Map<String, List<String>> generateVotingYetList(final PlaygroundVote playgroundVote) {
        List<String> dongList = playgroundVote.getVoterDongList();

        Map<String, List<String>> votingYetList = new HashMap<>();
        Set<PlaygroundVoter> notVotingVoters = playgroundVote.getVotersNotVoting();
        dongList.forEach(dong -> {
            List<String> hoList = notVotingVoters.stream()
                    .filter(voter -> voter.getDong().equals(dong))
                    .map(PlaygroundVoter::getHo)
                    .collect(Collectors.toList());
            votingYetList.put(dong, hoList);
        });

        return votingYetList;
    }

}
