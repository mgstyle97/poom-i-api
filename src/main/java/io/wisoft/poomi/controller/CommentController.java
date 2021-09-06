package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.CommentDeleteDto;
import io.wisoft.poomi.bind.dto.CommentModifiedDto;
import io.wisoft.poomi.bind.dto.CommentRegistDto;
import io.wisoft.poomi.bind.request.CommentModifiedRequest;
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

    @PostMapping("/class/{class_id}/comment")
    public ApiResponse<CommentRegistDto> registComment(
            @PathVariable("class_id") @Valid final Long classId,
            @RequestBody @Valid final CommentRegistRequest commentRegistRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.CREATED,
                commentService.registComment(classId, commentRegistRequest, member)
        );
    }

    @PatchMapping("/comment/{comment_id}")
    public ApiResponse<CommentModifiedDto> modifiedComment(
            @PathVariable("comment_id") @Valid final Long commentId,
            @RequestBody @Valid final CommentModifiedRequest commentModifiedRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                commentService.modifiedComment(commentId, commentModifiedRequest, member)
        );
    }

    @DeleteMapping("/comment/{comment_id}")
    public ApiResponse<CommentDeleteDto> removeComment(
            @PathVariable("comment_id") @Valid final Long commentId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                commentService.removeComment(commentId, member)
        );
    }

}
