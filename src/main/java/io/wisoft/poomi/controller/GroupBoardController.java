package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.configures.web.validator.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardModifyRequest;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.child_care.board.GroupBoardLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.board.GroupBoardRegisterResponse;
import io.wisoft.poomi.global.utils.DomainUtils;
import io.wisoft.poomi.service.child_care.board.GroupBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class GroupBoardController {

    private final GroupBoardService groupBoardService;

    @GetMapping
    public ApiResponse<List<GroupBoardLookupResponse>> lookupAllGroupBoard(@SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                groupBoardService.lookupAllGroupBoard(member)
        );
    }

    @PostMapping
    public ApiResponse<GroupBoardRegisterResponse> registerGroupBoard(
            @RequestBody @Valid final GroupBoardRegisterRequest registerRequest,
            @SignInMember final Member member, final HttpServletRequest request) {
        log.info("Received Request DTO: {}", registerRequest);

        return ApiResponse.succeed(HttpStatus.CREATED,
                groupBoardService
                        .registerGroupBoard(
                                member, registerRequest,
                                DomainUtils.generateDomainByRequest(request)
                        ));
    }

    @PatchMapping("/{board-id}")
    public void modifyGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @RequestBody @Valid final GroupBoardModifyRequest modifyRequest,
            @SignInMember final Member member,
            final HttpServletRequest request) {
        groupBoardService
                .modifyGroupBoard(boardId, member, modifyRequest, DomainUtils.generateDomainByRequest(request));
    }

    @DeleteMapping("/{board-id}")
    public void removeGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @SignInMember final Member member) {
        groupBoardService.removeGroupBoard(boardId, member);
    }

    @PostMapping("/{board-id}/like")
    public void likeGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @SignInMember final Member member) {
        groupBoardService.likeGroupBoard(boardId, member);
    }

    @DeleteMapping("/{board-id}/like")
    public void cancelLikeGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @SignInMember final Member member) {
        groupBoardService.cancelLikeGroupBoard(boardId, member);
    }

}
