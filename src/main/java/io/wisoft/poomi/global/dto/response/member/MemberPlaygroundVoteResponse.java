package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.PlaygroundVoteRealtimeInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberPlaygroundVoteResponse {

    @JsonProperty("voting_vote_list")
    private List<PlaygroundVoteRealtimeInfoResponse> votingVoteList;

    @JsonProperty("member_register_vote_list")
    private List<PlaygroundVoteRealtimeInfoResponse> memberRegisterVoteList;

    @Builder
    public MemberPlaygroundVoteResponse(final List<PlaygroundVoteRealtimeInfoResponse> votingVoteList,
                                        final List<PlaygroundVoteRealtimeInfoResponse> memberRegisterVoteList) {
        this.votingVoteList = votingVoteList;
        this.memberRegisterVoteList = memberRegisterVoteList;
    }

    public static MemberPlaygroundVoteResponse of(final Member member,
                                                  final List<PlaygroundVote> votingVoteList,
                                                  final List<PlaygroundVote> memberRegisterVoteList) {
        return MemberPlaygroundVoteResponse.builder()
                .votingVoteList(
                        votingVoteList.stream()
                                .map(vote -> PlaygroundVoteRealtimeInfoResponse.of(vote, member))
                                .collect(Collectors.toList())
                )
                .memberRegisterVoteList(
                        memberRegisterVoteList.stream()
                                .map(vote -> PlaygroundVoteRealtimeInfoResponse.of(vote, member))
                                .collect(Collectors.toList())
                )
                .build();
    }

}
