package io.wisoft.poomi.controller;

import io.wisoft.poomi.service.child_care.playground.PlaygroundVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/playground/vote")
public class PlaygroundVoteController {

    private final PlaygroundVoteService playgroundVoteService;

    @PostMapping
    public void registerPlaygroundVote() {

    }

}
