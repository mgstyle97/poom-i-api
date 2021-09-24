package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.childminder.classes.comment.CommentDeleteResponse;
import io.wisoft.poomi.global.dto.response.childminder.classes.comment.CommentModifiedResponse;
import io.wisoft.poomi.global.dto.response.childminder.classes.comment.CommentRegistResponse;
import io.wisoft.poomi.global.dto.request.childminder.comment.CommentModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.comment.CommentRegisterRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.childminder.comment.CommentService;
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
    public ApiResponse<CommentRegistResponse> registComment(
            @PathVariable("class-id") @Valid final Long classId,
            @RequestBody @Valid final CommentRegisterRequest commentRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.CREATED,
                commentService.registComment(classId, commentRegisterRequest, member)
        );
    }

    @PatchMapping("/comment/{comment-id}")
    public ApiResponse<CommentModifiedResponse> modifiedComment(
            @PathVariable("comment-id") @Valid final Long commentId,
            @RequestBody @Valid final CommentModifiedRequest commentModifiedRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                commentService.modifiedComment(commentId, commentModifiedRequest, member)
        );
    }

    @DeleteMapping("/comment/{comment-id}")
    public ApiResponse<CommentDeleteResponse> removeComment(
            @PathVariable("comment-id") @Valid final Long commentId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                commentService.removeComment(commentId, member)
        );
    }

}
