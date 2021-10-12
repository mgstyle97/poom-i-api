package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentDeleteResponse;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentModifiedResponse;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentRegistResponse;
import io.wisoft.poomi.global.dto.request.child_care.comment.CommentModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.comment.CommentRegisterRequest;
import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.service.child_care.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/board/{board-id}/comment")
    public ApiResponse<List<CommentLookupResponse>> lookupAllComment(
            @PathVariable("board-id") @Valid final Long boardId,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                commentService.lookupAllCommentByBoardId(boardId, member)
        );
    }

    @PostMapping("/board/{board-id}/comment")
    public ApiResponse<CommentRegistResponse> registerComment(
            @PathVariable("board-id") @Valid final Long boardId,
            @RequestBody @Valid final CommentRegisterRequest commentRegisterRequest,
            @SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.CREATED,
                commentService.registerComment(boardId, commentRegisterRequest, member)
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
