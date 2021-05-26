package io.wisoft.poomi.domain.board;

import io.wisoft.poomi.domain.board.dto.BoardModifiedDto;
import io.wisoft.poomi.domain.board.dto.BoardPostDto;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.dto.AuthInfo;
import io.wisoft.poomi.exception.InvalidApproachException;
import io.wisoft.poomi.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public List<Board> getAllBoard() {
        return boardRepository.findAll();
    }

    public Board boardById(final Long boardId) {
        Board board = boardRepository
                .findById(boardId)
                .orElseThrow(NotFoundException::new);
        board.increaseViews();

        return boardRepository.save(board);
    }

    public Board post(final AuthInfo authInfo, final BoardPostDto boardPostDto) {
        Member member = memberRepository
                .findByEmail(authInfo.getEmail())
                .orElseThrow(NotFoundException::new);

        return boardRepository.save(Board.of(member, boardPostDto));
    }

    public Board modified(final Long boardId,
                          final AuthInfo authInfo,
                          final BoardModifiedDto boardModifiedDto) {
        Member member = memberRepository
                .findByEmail(authInfo.getEmail())
                .orElseThrow(NotFoundException::new);
        Board board = boardRepository
                .findById(boardId)
                .orElseThrow(NotFoundException::new);
        if (member != board.getMember()) {
            throw new InvalidApproachException();
        }

        Board updatedBoard = changeBoardInfo(board, boardModifiedDto);
        updatedBoard.setModifiedAt(LocalDateTime.now());

        return boardRepository.save(updatedBoard);

    }

    public void delete(final Long boardId,
                       final AuthInfo authInfo) {
        Member member = memberRepository
                .findByEmail(authInfo.getEmail())
                .orElseThrow(NotFoundException::new);
        Board board = boardRepository
                .findById(boardId)
                .orElseThrow(NotFoundException::new);
        if (member != board.getMember()) {
            throw new InvalidApproachException();
        }

        boardRepository.delete(board);
    }

    private Board changeBoardInfo(final Board board, final BoardModifiedDto boardModifiedDto) {
        return board
                .changeTitle(boardModifiedDto.getTitle())
                .changeContents(boardModifiedDto.getContents());
    }

}
