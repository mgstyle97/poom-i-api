package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.CommentRegistDto;
import io.wisoft.poomi.bind.request.CommentRegistRequest;
import io.wisoft.poomi.configures.web.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/class/{class-id}/comment")
    public ApiResponse<CommentRegistDto> registComment(
            @PathVariable("class-id") @Valid final Long classId,
            @RequestBody @Valid final CommentRegistRequest commentRegistRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.CREATED,
                commentService.registComment(classId, commentRegistRequest, member)
        );
    }

}
