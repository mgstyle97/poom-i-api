package io.wisoft.poomi.service.child_care.board;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoardRepository;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.domain.child_care.group.image.ImageRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardModifyRequest;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import io.wisoft.poomi.global.dto.response.child_care.board.GroupBoardLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.board.GroupBoardRegisterResponse;
import io.wisoft.poomi.global.utils.FileUtils;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import io.wisoft.poomi.service.child_care.comment.CommentService;
import io.wisoft.poomi.service.child_care.group.ChildCareGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupBoardService {

    private final ChildCareGroupRepository childCareGroupRepository;
    private final GroupBoardRepository groupBoardRepository;
    private final ImageRepository imageRepository;

    private final ChildCareGroupService childCareGroupService;
    private final CommentService commentService;

    @Transactional(readOnly = true)
    public List<GroupBoardLookupResponse> lookupAllGroupBoard(final Member member) {
        List<ChildCareGroup> groups = childCareGroupRepository.findAllByAddressTag(member.getAddressTag());

        List<Set<GroupBoard>> boardsList = groups.stream().map(ChildCareGroup::getBoards).collect(Collectors.toList());
        List<GroupBoard> boards = mergeBoards(boardsList);

        return boards.stream()
                .map(GroupBoardLookupResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupBoardRegisterResponse registerGroupBoard(final Member member,
                                                         final GroupBoardRegisterRequest registerRequest,
                                                         final List<MultipartFile> images,
                                                         final String domainInfo) {
        ChildCareGroup childCareGroup = childCareGroupService.generateChildCareGroupById(registerRequest.getGroupId());

        childCareGroup.validateMemberIsParticipating(member);
        log.info("Check member is participating in group");

        GroupBoard board = GroupBoard.of(registerRequest, childCareGroup, member);
        groupBoardRepository.save(board);
        childCareGroup.addBoard(board);
        log.info("Save board through request id: {}", board.getId());

        saveImages(board, images, domainInfo);
        log.info("Save images and set in board");

        return new GroupBoardRegisterResponse(board);
    }

    @Transactional
    public void modifyGroupBoard(final Long boardId, final Member member,
                                 final GroupBoardModifyRequest modifyRequest,
                                 final List<MultipartFile> images,
                                 final String domainInfo) {
        GroupBoard board = generateGroupBoard(boardId);

        ContentPermissionVerifier.verifyModifyPermission(board.getWriter(), member);

        modifyBoard(board, modifyRequest, images, domainInfo);
        groupBoardRepository.save(board);

    }

    @Transactional
    public void removeGroupBoard(final Long boardId, final Member member) {
        GroupBoard board = generateGroupBoard(boardId);

        ContentPermissionVerifier.verifyModifyPermission(board.getWriter(), member);

        removeBoard(board);
    }

    @Transactional
    public void likeGroupBoard(final Long boardId, final Member member) {
        GroupBoard board = generateGroupBoard(boardId);

        board.addLikeMember(member);

    }

    @Transactional
    public void cancelLikeGroupBoard(final Long boardId, final Member member) {
        GroupBoard board = generateGroupBoard(boardId);

        board.removeLikeMember(member);
    }

    private GroupBoard generateGroupBoard(final Long boardId) {
        GroupBoard board = groupBoardRepository.getBoardById(boardId);
        log.info("Generate group board id: {}", boardId);

        return board;
    }

    private void saveImages(final GroupBoard board, final List<MultipartFile> images, final String domainInfo) {
        Set<Image> savedLocalImages = FileUtils.saveImageWithBoardId(board, images, domainInfo);

        if (!CollectionUtils.isEmpty(savedLocalImages)) {
            imageRepository.saveAll(savedLocalImages);
            savedLocalImages.forEach(board::addImage);
        }

    }

    private List<GroupBoard> mergeBoards(List<Set<GroupBoard>> boardsList) {
        return boardsList.stream()
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

    private void modifyBoard(final GroupBoard board,
                             final GroupBoardModifyRequest modifyRequest,
                             final List<MultipartFile> images,
                             final String domainInfo) {
        Optional.ofNullable(modifyRequest.getGroupId()).ifPresent(groupId -> {
            Optional<ChildCareGroup> optionalGroup = childCareGroupRepository.findById(groupId);
            optionalGroup.ifPresent(board::changeGroup);
        });

        board.changeContents(modifyRequest.getContents());

        Optional.ofNullable(modifyRequest.getRemoveImageIds()).ifPresent(removeImageIds -> {
            removeImageIds.stream()
                    .map(imageRepository::findById)
                    .forEach(optionalImage -> optionalImage.ifPresent(image -> {
                        board.removeImage(image);
                        imageRepository.delete(image);
                        FileUtils.removeImage(image);
                    }));
        });

        Optional.ofNullable(images).ifPresent(imageList -> {
            saveImages(board, imageList, domainInfo);
        });

    }

    private void removeBoard(final GroupBoard board) {
        board.resetAssociated();

        Set<Image> images = board.getImages();
        FileUtils.removeBoardImages(images);
        imageRepository.deleteAll(images);

        Set<Comment> comments = board.getComments();
        commentService.deleteAll(comments, board.getId());

        groupBoardRepository.delete(board);
    }

}
