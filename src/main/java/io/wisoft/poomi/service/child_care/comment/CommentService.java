package io.wisoft.poomi.service.child_care.comment;

import io.wisoft.poomi.global.dto.response.child_care.group.comment.CommentDeleteResponse;
import io.wisoft.poomi.global.dto.response.child_care.group.comment.CommentModifiedResponse;
import io.wisoft.poomi.global.dto.response.child_care.group.comment.CommentRegistResponse;
import io.wisoft.poomi.global.dto.request.child_care.group.comment.CommentModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.comment.CommentRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.child_care.group.comment.CommentRepository;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ChildCareGroupRepository childCareGroupRepository;

    @Transactional
    public CommentRegistResponse registComment(final Long classId,
                                               final CommentRegisterRequest commentRegisterRequest,
                                               final Member member) {
        ChildCareGroup childCareGroup = childCareGroupRepository.getById(classId);
        log.info("Generate class program id: {}", classId);

        Comment comment = Comment.of(commentRegisterRequest, member, childCareGroup);
        log.info("Generate comment entity from request");

        commentRepository.save(comment);
        log.info("Save comment data id: {}", comment.getId());

        return CommentRegistResponse.of(comment);
    }

    @Transactional
    public CommentModifiedResponse modifiedComment(final Long commentId,
                                                   final CommentModifiedRequest commentModifiedRequest,
                                                   final Member member) {
        Comment comment = generateComment(commentId);

        ContentPermissionVerifier.verifyModifyPermission(comment.getWriter(), member);

        comment.updateContents(commentModifiedRequest.getContents());
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
    public void deleteAll(final Set<Comment> comments, final Long classId) {
        commentRepository.deleteAll(comments);
        log.info("Delete all comments data contained in class program id: {}", classId);
    }

    private Comment generateComment(final Long commentId) {
        Comment comment = commentRepository.getCommentById(commentId);
        log.info("Generate comment by id: {}", commentId);

        return comment;
    }

}
