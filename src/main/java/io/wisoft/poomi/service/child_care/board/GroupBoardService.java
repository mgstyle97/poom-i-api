package io.wisoft.poomi.service.child_care.board;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoardRepository;
import io.wisoft.poomi.domain.child_care.group.comment.Comment;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardModifyRequest;
import io.wisoft.poomi.global.dto.request.child_care.board.GroupBoardRegisterRequest;
import io.wisoft.poomi.global.dto.response.child_care.board.GroupBoardLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.board.GroupBoardRegisterResponse;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import io.wisoft.poomi.service.child_care.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
    private final UploadFileRepository uploadFileRepository;
    private final UploadFileUtils uploadFileUtils;

    private final CommentService commentService;

    @Transactional(readOnly = true)
    public List<GroupBoardLookupResponse> lookupAllGroupBoard(final Member member) {
        List<ChildCareGroup> groups = childCareGroupRepository.findAllByAddressTag(member.getAddressTag());

        List<Set<GroupBoard>> boardsList = groups.stream().map(ChildCareGroup::getBoards).collect(Collectors.toList());
        List<GroupBoard> boards = mergeBoards(boardsList);

        return boards.stream()
                .map(board-> GroupBoardLookupResponse.of(board, member))
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupBoardRegisterResponse registerGroupBoard(final Member member,
                                                         final GroupBoardRegisterRequest registerRequest) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(registerRequest.getGroupId());

        childCareGroup.validateMemberIsParticipating(member);
        log.info("Check member is participating in group");

        GroupBoard board = GroupBoard.of(registerRequest, childCareGroup, member);
        groupBoardRepository.save(board);
        childCareGroup.addBoard(board);
        log.info("Save board through request id: {}", board.getId());

        saveImages(board, registerRequest.getImage());

        return new GroupBoardRegisterResponse(board);
    }

    @Transactional
    public void modifyGroupBoard(final Long boardId, final Member member,
                                 final GroupBoardModifyRequest modifyRequest) {
        GroupBoard board = generateGroupBoard(boardId);

        ContentPermissionVerifier.verifyModifyPermission(board.getWriter(), member);

        modifyBoard(board, modifyRequest);

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

    private ChildCareGroup generateChildCareGroupById(final Long groupId) {
        return childCareGroupRepository.getById(groupId);
    }

    private GroupBoard generateGroupBoard(final Long boardId) {
        GroupBoard board = groupBoardRepository.getBoardById(boardId);
        log.info("Generate group board id: {}", boardId);

        return board;
    }

    private void saveImages(final GroupBoard board, final String imageData) {
        Optional<String> optionalImageData = Optional.ofNullable(imageData);

        if (optionalImageData.isPresent()) {
            UploadFile image  = uploadFileUtils.saveFileAndConvertImage(optionalImageData.get());

            if (!ObjectUtils.isEmpty(image)) {
                uploadFileRepository.save(image);
                board.addImage(image);
                log.info("Save images and set in board");
            }
        }

    }

    private List<GroupBoard> mergeBoards(List<Set<GroupBoard>> boardsList) {
        return boardsList.stream()
                .flatMap(Set::stream)
                .collect(Collectors.toList());
    }

    private void modifyBoard(final GroupBoard board,
                             final GroupBoardModifyRequest modifyRequest) {
        Optional.ofNullable(modifyRequest.getGroupId()).ifPresent(groupId -> {
            Optional<ChildCareGroup> optionalGroup = childCareGroupRepository.findById(groupId);
            optionalGroup.ifPresent(board::changeGroup);
        });

        board.changeContents(modifyRequest.getContents());

        Optional.ofNullable(modifyRequest.getRemoveImageIds()).ifPresent(removeImageIds -> {
            removeImageIds.stream()
                    .map(uploadFileRepository::findById)
                    .forEach(optionalImage -> optionalImage.ifPresent(image -> {
                        board.removeImage(image);
                        uploadFileRepository.delete(image);
                        uploadFileUtils.removeImage(image);
                    }));
        });

        Optional.ofNullable(modifyRequest.getImageDataList()).ifPresent(imageDataList -> {
            imageDataList.forEach(imageData -> this.saveImages(board, imageData));
        });

    }

    public void removeBoard(final GroupBoard board) {
        board.resetAssociated();

        Set<UploadFile> images = board.getImages();
        uploadFileRepository.deleteAll(images);
        uploadFileUtils.removeBoardImages(images);

        Set<Comment> comments = board.getComments();
        commentService.deleteAll(comments, board.getId());

        groupBoardRepository.delete(board);
    }

}
