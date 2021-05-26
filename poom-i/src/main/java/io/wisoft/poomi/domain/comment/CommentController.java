package io.wisoft.poomi.domain.comment;

import io.wisoft.poomi.domain.comment.dto.CommentModifiedDto;
import io.wisoft.poomi.domain.comment.dto.CommentPostDto;
import io.wisoft.poomi.domain.member.dto.AuthInfo;
import io.wisoft.poomi.response.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/boards/{id}/comments")
    public ApiResult<?> post(@PathVariable("id") @Valid final Long boardId,
                             @RequestBody @Valid final CommentPostDto commentPostDto,
                             HttpSession session) {
        Comment comment = commentService.post(boardId, (AuthInfo) session.getAttribute("USER_INFO"), commentPostDto);

        return ApiResult.get(HttpStatus.CREATED, null, "Create Success");
    }

    @PutMapping("/boards/{boardId}/comments/{commentId}")
    public ApiResult<?> modified(@PathVariable("boardId") @Valid final Long boardId,
                                 @PathVariable("commentId") @Valid final Long commentId,
                                 @RequestBody @Valid final CommentModifiedDto commentModifiedDto,
                                 HttpSession httpSession) {
        Comment updatedComment = commentService.modified(
                boardId, commentId, commentModifiedDto,
                (AuthInfo) httpSession.getAttribute("USER_INFO"));

        return ApiResult.get(HttpStatus.OK, null, "Modified Success");
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    public ApiResult<?> delete(@PathVariable("boardId") @Valid final Long boardId,
                               @PathVariable("commentId") @Valid final Long commentId,
                               HttpSession session) {
        commentService.delete(boardId, commentId, (AuthInfo) session.getAttribute("USER_INFO"));

        return ApiResult.get(HttpStatus.OK, null, "Delete Success");
    }

}
