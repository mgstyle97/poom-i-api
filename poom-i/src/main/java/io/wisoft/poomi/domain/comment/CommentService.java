package io.wisoft.poomi.domain.comment;

import io.wisoft.poomi.domain.board.Board;
import io.wisoft.poomi.domain.board.BoardRepository;
import io.wisoft.poomi.domain.comment.dto.CommentModifiedDto;
import io.wisoft.poomi.domain.comment.dto.CommentPostDto;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.dto.AuthInfo;
import io.wisoft.poomi.exception.InvalidApproachException;
import io.wisoft.poomi.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Comment post(final Long boardId,
                        final AuthInfo authInfo,
                        final CommentPostDto commentPostDto) {

        Member member = memberRepository
                .findByEmail(authInfo.getEmail())
                .orElseThrow(NotFoundException::new);

        Board board = boardRepository
                .findById(boardId)
                .orElseThrow(NotFoundException::new);

        Comment comment = Comment.of(member, board, commentPostDto);

        board.getComments().add(comment);
        boardRepository.save(board);

        return commentRepository.save(comment);

    }

    public Comment modified(final Long boardId, final Long commentId,
                            final CommentModifiedDto commentModifiedDto,
                            final AuthInfo authInfo) {
        Member member = memberRepository
                .findByEmail(authInfo.getEmail())
                .orElseThrow(NotFoundException::new);
        Board board = boardRepository
                .findById(boardId)
                .orElseThrow(NotFoundException::new);
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(NotFoundException::new);
        if (member != comment.getMember()) {
            throw new InvalidApproachException();
        }
        comment = comment
                .changeContents(commentModifiedDto.getContents())
                .changeIsSecret(comment.isSecret());
        return commentRepository.save(comment);
    }

    public void delete(final Long boardId, final Long commentId,
                       final AuthInfo authInfo) {
        Member member = memberRepository
                .findByEmail(authInfo.getEmail())
                .orElseThrow(NotFoundException::new);
        Board board = boardRepository
                .findById(boardId)
                .orElseThrow(NotFoundException::new);
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(NotFoundException::new);
        if ((member != board.getMember()) ||
                (member != comment.getMember())) {
            throw new InvalidApproachException();
        }

        commentRepository.delete(comment);
    }
}
