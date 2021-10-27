package io.wisoft.poomi.global.dto.request.child_care.playground;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.VoteType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaygroundVoteVotingRequest {

    private String dong;

    private String ho;

    @JsonProperty("vote_type")
    private VoteType voteType;

    public void setVoteType(final String voteType) {
        this.voteType = VoteType.valueOf(voteType);
    }

}
