package io.wisoft.poomi.service.childminder.comment;

import io.wisoft.poomi.global.dto.response.childminder.classes.comment.CommentDeleteResponse;
import io.wisoft.poomi.global.dto.response.childminder.classes.comment.CommentModifiedResponse;
import io.wisoft.poomi.global.dto.response.childminder.classes.comment.CommentRegistResponse;
import io.wisoft.poomi.global.dto.request.childminder.classes.CommentModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.classes.CommentRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClassRepository;
import io.wisoft.poomi.domain.childminder.classes.comment.Comment;
import io.wisoft.poomi.domain.childminder.classes.comment.CommentRepository;
import io.wisoft.poomi.service.childminder.ContentPermissionVerifier;
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
    private final ChildminderClassRepository childminderClassRepository;

    @Transactional
    public CommentRegistResponse registComment(final Long classId,
                                               final CommentRegisterRequest commentRegisterRequest,
                                               final Member member) {
        ChildminderClass childminderClass = childminderClassRepository.getById(classId);
        log.info("Generate class program id: {}", classId);

        Comment comment = Comment.of(commentRegisterRequest, member, childminderClass);
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

        ContentPermissionVerifier.verifyPermission(comment.getWriter(), member);

        comment.updateContents(commentModifiedRequest.getContents());
        log.info("Update comment entity: {}", commentId);

        return CommentModifiedResponse.of(comment);
    }

    @Transactional
    public CommentDeleteResponse removeComment(final Long commentId,
                                               final Member member) {
        Comment comment = generateComment(commentId);

        ContentPermissionVerifier.verifyPermission(comment.getWriter(), member);

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
