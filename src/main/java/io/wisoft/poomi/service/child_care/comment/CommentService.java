package io.wisoft.poomi.service.child_care.comment;

import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoardRepository;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentDeleteResponse;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentModifiedResponse;
import io.wisoft.poomi.global.dto.response.child_care.comment.CommentRegistResponse;
import io.wisoft.poomi.global.dto.request.child_care.comment.CommentModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.comment.CommentRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.child_care.group.comment.CommentRepository;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final GroupBoardRepository groupBoardRepository;
    private final ChildCareGroupRepository childCareGroupRepository;

    @Transactional(readOnly = true)
    public List<CommentLookupResponse> lookupAllCommentByBoardId(final Long boardId, final Member member) {
        GroupBoard board = groupBoardRepository.getBoardById(boardId);

        return board.getComments().stream()
                .map(CommentLookupResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentRegistResponse registerComment(final Long boardId,
                                                 final CommentRegisterRequest commentRegisterRequest,
                                                 final Member member) {
        GroupBoard board = groupBoardRepository.getBoardById(boardId);
        log.info("Generate board id: {}", boardId);

        Comment comment = Comment.of(commentRegisterRequest, member, board);
        commentRepository.save(comment);
        log.info("Generate comment and save comment id: {}", comment.getId());

        return CommentRegistResponse.of(comment);
    }

    @Transactional
    public CommentModifiedResponse modifiedComment(final Long commentId,
                                                   final CommentModifiedRequest commentModifiedRequest,
                                                   final Member member) {
        Comment comment = generateComment(commentId);

        ContentPermissionVerifier.verifyModifyPermission(comment.getWriter(), member);

        comment.changeContents(commentModifiedRequest.getContents());
        log.info("Update comment entity: {}", commentId);

        return CommentModifiedResponse.of(comment);
    }

    @Transactional
    public CommentDeleteResponse removeComment(final Long commentId,
                                               final Member member) {
        Comment comment = generateComment(commentId);

        ContentPermissionVerifier.verifyModifyPermission(comment.getWriter(), member);

        commentRepository.delete(comment);
        log.info("Delete comment entity id: {}", commentId);

        return CommentDeleteResponse.of(comment);
    }

    @Transactional
    public void deleteAll(final Set<Comment> comments, final Long boardId) {
        commentRepository.deleteAll(comments);
        log.info("Delete all comments data contained in group board id: {}", boardId);
    }

    private Comment generateComment(final Long commentId) {
        Comment comment = commentRepository.getCommentById(commentId);
        log.info("Generate comment by id: {}", commentId);

        return comment;
    }

}
