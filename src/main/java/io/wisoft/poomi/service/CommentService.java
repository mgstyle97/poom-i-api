package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.CommentRegistDto;
import io.wisoft.poomi.bind.request.CommentRegistRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.domain.program.classes.ClassProgramRepository;
import io.wisoft.poomi.domain.program.classes.comment.Comment;
import io.wisoft.poomi.domain.program.classes.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.info("Save commment data id: {}", comment.getId());

        return CommentRegistDto.of(comment);
    }

}
