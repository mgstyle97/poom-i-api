package io.wisoft.poomi.domain.board;

import io.wisoft.poomi.domain.board.dto.BoardModifiedDto;
import io.wisoft.poomi.domain.board.dto.BoardPostDto;
import io.wisoft.poomi.domain.member.dto.AuthInfo;
import io.wisoft.poomi.response.ApiResult;
import io.wisoft.poomi.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ApiResult<List<Board>> getAllBoard() {
        List<Board> boards = boardService.getAllBoard();

        return ApiResult.get(HttpStatus.OK, boards, "Search Success");
    }

    @GetMapping("/{id}")
    public ApiResult<?> getBoardById(@PathVariable("id") @Valid final Long boardId) {
        Board board = boardService.boardById(boardId);

        log.info("board : {}", board);

        return ApiResult.get(HttpStatus.OK, board, "Search Success");
    }

    @PostMapping
    public ApiResult<BoardResponse> post(@RequestBody@Valid final BoardPostDto boardPostDto,
                                         HttpSession session) {
        Board newBoard = boardService.post((AuthInfo) session.getAttribute("USER_INFO"), boardPostDto);

        log.info("new board : {}", newBoard);

        return ApiResult.get(HttpStatus.CREATED, BoardResponse.get(newBoard), "Create Success");
    }

    @PutMapping("/{id}")
    public ApiResult<BoardResponse> modified(@PathVariable("id") @Valid final Long boardId,
                                             @RequestBody @Valid final BoardModifiedDto boardModifiedDto,
                                             HttpSession session) {
        Board updatedBoard = boardService.modified(
                boardId,
                (AuthInfo) session.getAttribute("USER_INFO"),
                boardModifiedDto
        );

        log.info("updated board : {}", updatedBoard);

        return ApiResult.get(HttpStatus.OK, BoardResponse.get(updatedBoard), "Modified Success");
    }

    @DeleteMapping("/{id}")
    public ApiResult<?> delete(@PathVariable("id") @Valid final Long boardId,
                               HttpSession session) {
        boardService.delete(boardId, (AuthInfo) session.getAttribute("USER_INFO"));

        return ApiResult.get(HttpStatus.OK, null, "Delete Success");
    }

}
