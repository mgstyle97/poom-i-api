package io.wisoft.poomi.service.child_care.board;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoardRepository;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.domain.child_care.group.image.ImageRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import io.wisoft.poomi.global.utils.FileUtils;
import io.wisoft.poomi.service.child_care.group.ChildCareGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupBoardService {

    private final ChildCareGroupRepository childCareGroupRepository;
    private final GroupBoardRepository groupBoardRepository;
    private final ImageRepository imageRepository;

    private final ChildCareGroupService childCareGroupService;

    @Transactional
    public void registerGroupBoard(final Member member,
                                   final GroupBoardRegisterRequest registerRequest,
                                   final List<MultipartFile> images,
                                   final String domainInfo) {
        ChildCareGroup childCareGroup = childCareGroupService.generateChildCareGroupById(registerRequest.getGroupId());

        childCareGroup.validateMemberIsParticipating(member);
        log.info("Check member is participating in group");

        GroupBoard board = GroupBoard.of(registerRequest, childCareGroup, member);
        groupBoardRepository.save(board);
        log.info("Save board through request id: {}", board.getId());

        saveImages(board, images, domainInfo);
        log.info("Save images and set in board");

    }

    private void saveImages(final GroupBoard board, final List<MultipartFile> images, final String domainInfo) {
        Set<Image> savedLocalImages = FileUtils.saveImageWithBoardId(board, images, domainInfo);

        if (!CollectionUtils.isEmpty(savedLocalImages)) {
            imageRepository.saveAll(savedLocalImages);
            savedLocalImages.forEach(board::addImage);
        }

    }

}
