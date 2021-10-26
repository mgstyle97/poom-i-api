package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.playground.PlaygroundVoteRegisterRequest;
import io.wisoft.poomi.service.child_care.playground.PlaygroundVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/playground/vote")
public class PlaygroundVoteController {

    private final PlaygroundVoteService playgroundVoteService;

    @PostMapping
    public void registerPlaygroundVote(
            @RequestBody @Valid final PlaygroundVoteRegisterRequest registerRequest,
            @SignInMember final Member member) {
        playgroundVoteService.registerPlaygroundVote(registerRequest, member);
    }

}
