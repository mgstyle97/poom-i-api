package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.CommentDeleteDto;
import io.wisoft.poomi.bind.dto.CommentModifiedDto;
import io.wisoft.poomi.bind.dto.CommentRegistDto;
import io.wisoft.poomi.bind.request.CommentModifiedRequest;
import io.wisoft.poomi.bind.request.CommentRegistRequest;
import io.wisoft.poomi.common.error.exceptions.AuthorizationException;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.domain.program.classes.ClassProgramRepository;
import io.wisoft.poomi.domain.program.classes.comment.Comment;
import io.wisoft.poomi.domain.program.classes.comment.CommentRepository;
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
    private final ClassProgramRepository classProgramRepository;

    @Transactional
    public CommentRegistDto registComment(final Long classId,
                                final CommentRegistRequest commentRegistRequest,
                                final Member member) {
        ClassProgram classProgram = classProgramRepository.findClassProgramById(classId);
        log.info("Generate class program id: {}", classId);

        Comment comment = Comment.of(commentRegistRequest, member, classProgram);
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
