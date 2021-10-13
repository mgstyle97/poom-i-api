package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.configures.web.validator.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardModifyRequest;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.child_care.board.GroupBoardLookupResponse;
import io.wisoft.poomi.service.child_care.board.GroupBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GroupBoardController {

    private final GroupBoardService groupBoardService;

    @GetMapping("/board")
    public ApiResponse<List<GroupBoardLookupResponse>> lookupAllGroupBoard(@SignInMember final Member member) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                groupBoardService.lookupAllGroupBoard(member)
        );
    }

    @PostMapping("/board")
    public void registerGroupBoard(
            @RequestPart("data") @Valid final GroupBoardRegisterRequest registerRequest,
            @RequestPart(value = "images", required = false) @Image List<MultipartFile> images,
            @SignInMember final Member member, final HttpServletRequest request) {
        groupBoardService.registerGroupBoard(member, registerRequest, images, getDomainInfo(request));
    }

    @PatchMapping("/board/{board-id}")
    public void modifyGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @RequestBody @Valid final GroupBoardModifyRequest modifyRequest,
            @SignInMember final Member member) {
        groupBoardService.modifyGroupBoard(boardId, member, modifyRequest);
    }

    @DeleteMapping("/board/{board-id}")
    public void removeGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @SignInMember final Member member) {
        groupBoardService.removeGroupBoard(boardId, member);
    }

    @PostMapping("/board/{board-id}/like")
    public void likeGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @SignInMember final Member member) {
        groupBoardService.likeGroupBoard(boardId, member);
    }

    @DeleteMapping("/board/{board-id}/like")
    public void cancelLikeGroupBoard(
            @PathVariable("board-id") @Valid final Long boardId,
            @SignInMember final Member member) {
        groupBoardService.cancelLikeGroupBoard(boardId, member);
    }

    private String getDomainInfo(final HttpServletRequest request) {
        return request
                .getRequestURL()
                .toString()
                .replace(request.getRequestURI(), "");
    }

}
