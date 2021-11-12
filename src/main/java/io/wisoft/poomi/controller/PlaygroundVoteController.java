package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.playground.PlaygroundVoteRegisterRequest;
import io.wisoft.poomi.global.dto.request.child_care.playground.PlaygroundVoteVotingRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.PlaygroundVoteLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.PlaygroundVoteRealtimeInfoResponse;
import io.wisoft.poomi.global.dto.response.member.MemberPlaygroundVoteResponse;
import io.wisoft.poomi.service.child_care.playground.PlaygroundVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PlaygroundVoteController {

    private final PlaygroundVoteService playgroundVoteService;

    @PostMapping("/playground/vote")
    public void registerPlaygroundVote(
            @RequestBody @Valid final PlaygroundVoteRegisterRequest registerRequest,
            @SignInMember final Member member) {
        playgroundVoteService.registerPlaygroundVote(registerRequest, member);
    }

    @GetMapping("/playground/vote")
    public ApiResponse<MemberPlaygroundVoteResponse> lookupPlaygroundVoteList(
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                playgroundVoteService.lookupPlaygroundVoteList(member)
        );
    }

    @GetMapping("/vote/{vote-id}")
    public ApiResponse<PlaygroundVoteLookupResponse> lookupPlaygroundVote(
            @PathVariable("vote-id") @Valid final Long voteId) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                playgroundVoteService.lookupPlaygroundVote(voteId)
        );
    }

    @PostMapping("/vote/{vote-id}")
    public void votingPlaygroundVote(
            @PathVariable("vote-id") @Valid final Long voteId,
            @RequestBody @Valid final PlaygroundVoteVotingRequest votingRequest) {
        playgroundVoteService.votingPlaygroundVote(voteId, votingRequest);
    }

}
