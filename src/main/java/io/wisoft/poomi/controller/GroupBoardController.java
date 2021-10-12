package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.configures.web.validator.image.Image;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import io.wisoft.poomi.service.child_care.board.GroupBoardService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/board")
    public void registerGroupBoard(
            @RequestPart("data") @Valid final GroupBoardRegisterRequest registerRequest,
            @RequestPart(value = "images", required = false) @Image List<MultipartFile> images,
            @SignInMember final Member member, final HttpServletRequest request) {
        groupBoardService.registerGroupBoard(member, registerRequest, images, getDomainInfo(request));
    }

    private String getDomainInfo(final HttpServletRequest request) {
        return request
                .getRequestURL()
                .toString()
                .replace(request.getRequestURI(), "");
    }

}
