package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.childminder.classes.comment.CommentDeleteDto;
import io.wisoft.poomi.bind.dto.childminder.classes.comment.CommentModifiedDto;
import io.wisoft.poomi.bind.dto.childminder.classes.comment.CommentRegistDto;
import io.wisoft.poomi.bind.request.childminder.classes.CommentModifiedRequest;
import io.wisoft.poomi.bind.request.childminder.classes.CommentRegisterRequest;
import io.wisoft.poomi.common.error.exceptions.AuthorizationException;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClassRepository;
import io.wisoft.poomi.domain.childminder.classes.comment.Comment;
import io.wisoft.poomi.domain.childminder.classes.comment.CommentRepository;
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
    public CommentRegistDto registComment(final Long classId,
                                final CommentRegisterRequest commentRegisterRequest,
                                final Member member) {
        ChildminderClass childminderClass = childminderClassRepository.findChildminderClassById(classId);
        log.info("Generate class program id: {}", classId);

        Comment comment = Comment.of(commentRegisterRequest, member, childminderClass);
        log.info("Generate comment entity from request");

        commentRepository.save(comment);
        log.info("Save comment data id: {}", comment.getId());

        return CommentRegistDto.of(comment);
    }

    @Transactional
    public CommentModifiedDto modifiedComment(final Long commentId,
                                  final CommentModifiedRequest commentModifiedRequest,
                                  final Member member) {
        Comment comment = generateComment(commentId);

        authorizationComment(comment, member);

        comment.updateContents(commentModifiedRequest.getContents());
        log.info("Update comment entity: {}", commentId);

        return CommentModifiedDto.of(comment);
    }

    @Transactional
    public CommentDeleteDto removeComment(final Long commentId,
                                               final Member member) {
        Comment comment = generateComment(commentId);

        authorizationComment(comment, member);

        commentRepository.delete(comment);
        log.info("Delete comment entity id: {}", commentId);

        return CommentDeleteDto.of(comment);
    }

    @Transactional
    void deleteAll(final Set<Comment> comments, final Long classId) {
        commentRepository.deleteAll(comments);
        log.info("Delete all comments data contained in class program id: {}", classId);
    }

    private Comment generateComment(final Long commentId) {
        Comment comment = commentRepository.getCommentById(commentId);
        log.info("Generate comment by id: {}", commentId);

        return comment;
    }

    private void authorizationComment(final Comment comment, final Member member) {
        if (!comment.getWriter().getId().equals(member.getId())) {
            throw new AuthorizationException("No match comment writer");
        }
    }

}
