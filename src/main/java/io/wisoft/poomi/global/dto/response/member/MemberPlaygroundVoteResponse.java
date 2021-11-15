package io.wisoft.poomi.global.dto.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.common.ApprovalStatus;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.PlaygroundVoteRealtimeInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberPlaygroundVoteResponse {

    @JsonProperty("member_residence_approval_status")
    private ApprovalStatus approvalStatus;

    @JsonProperty("voting_vote_list")
    private List<PlaygroundVoteRealtimeInfoResponse> votingVoteList;

    @JsonProperty("member_register_vote_list")
    private List<PlaygroundVoteRealtimeInfoResponse> memberRegisterVoteList;

    @Builder
    public MemberPlaygroundVoteResponse(final ApprovalStatus approvalStatus,
                                        final List<PlaygroundVoteRealtimeInfoResponse> votingVoteList,
                                        final List<PlaygroundVoteRealtimeInfoResponse> memberRegisterVoteList) {
        this.approvalStatus = approvalStatus;
        this.votingVoteList = votingVoteList;
        this.memberRegisterVoteList = memberRegisterVoteList;
    }

    public static MemberPlaygroundVoteResponse of(final ApprovalStatus approvalStatus,
                                                  final List<PlaygroundVote> votingVoteList,
                                                  final List<PlaygroundVote> memberRegisterVoteList) {
        return MemberPlaygroundVoteResponse.builder()
                .approvalStatus(approvalStatus)
                .votingVoteList(
                        votingVoteList.stream()
                                .map(PlaygroundVoteRealtimeInfoResponse::of)
                                .collect(Collectors.toList())
                )
                .memberRegisterVoteList(
                        memberRegisterVoteList.stream()
                                .map(PlaygroundVoteRealtimeInfoResponse::ofNotVotingList)
                                .collect(Collectors.toList())
                )
                .build();
    }

}
