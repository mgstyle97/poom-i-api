package io.wisoft.poomi.service.child_care.group;

import io.wisoft.poomi.domain.child_care.group.apply.GroupApply;
import io.wisoft.poomi.domain.child_care.group.apply.GroupApplyRepository;
import io.wisoft.poomi.domain.child_care.group.participating.GroupParticipatingMember;
import io.wisoft.poomi.domain.child_care.group.participating.GroupParticipatingMemberRepository;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.aop.child_care.NoAccessCheck;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupApplyRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupSimpleDataResponse;
import io.wisoft.poomi.global.dto.response.child_care.group.*;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.global.exception.exceptions.AlreadyExistsGroupNameException;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import io.wisoft.poomi.service.child_care.board.GroupBoardService;
import io.wisoft.poomi.service.file.S3FileHandler;
import io.wisoft.poomi.service.member.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildCareGroupService {

    private final ChildCareGroupRepository childCareGroupRepository;
    private final GroupParticipatingMemberRepository groupParticipatingMemberRepository;
    private final GroupApplyRepository groupApplyRepository;
    private final UploadFileRepository uploadFileRepository;
    private final S3FileHandler s3FileHandler;
    private final UploadFileUtils uploadFileUtils;

    private final ChildService childService;
    private final GroupBoardService groupBoardService;

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildCareGroupSimpleDataResponse> groupSimpleList(final Member member) {
        Set<ChildCareGroup> participatingGroups = member.getChildCareGroupProperties().getParticipatingGroups().stream()
                .map(GroupParticipatingMember::getGroup)
                .collect(Collectors.toSet());

        return participatingGroups.stream()
                .map(ChildCareGroupSimpleDataResponse::of)
                .collect(Collectors.toList());
    }

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildCareGroupLookupResponse> findByAddressTag(final AddressTag addressTag) {
        return childCareGroupRepository.findAllByAddressTag(addressTag).stream()
                .map(ChildCareGroupLookupResponse::new)
                .collect(Collectors.toList());
    }


    @NoAccessCheck
    @Transactional(readOnly = true)
    public byte[] getProfileImage(final String groupName) {
        final ChildCareGroup group = childCareGroupRepository.findByName(groupName).orElseThrow(
                () -> new NotFoundEntityDataException("group name: " + groupName + "에 관한 품앗이반이 없습니다.")
        );

        return s3FileHandler.getFileData(group.getProfileImage().getFileName());
    }

    @NoAccessCheck
    @Transactional
    public ChildCareGroupRegisterResponse registerChildCareGroup(
            final Member member,
            final ChildCareGroupRegisterRequest childCareGroupRegisterRequest) {
        validateGroupName(childCareGroupRegisterRequest.getName());

        ChildCareGroup group = ChildCareGroup.of(member, childCareGroupRegisterRequest);
        log.info("Generate child care group title: {}", group.getName());

        Optional.ofNullable(childCareGroupRegisterRequest.getProfileImage()).ifPresent(imageData -> {
            UploadFile profileImage = uploadFileUtils.saveFileAndConvertImage(imageData);
            uploadFileRepository.save(profileImage);
            group.setProfileImage(profileImage);
        });

        childCareGroupRepository.save(group);
        log.info("Save child care group id: {}", group.getId());

        GroupParticipatingMember participatingMember = GroupParticipatingMember.builder()
                .member(member)
                .group(group)
                .participationType(ParticipationType.MANAGE)
                .build();
        groupParticipatingMemberRepository.save(participatingMember);
        group.addParticipatingMember(participatingMember);
        log.info("Save participating group with writer id: {}", member.getId());

        return ChildCareGroupRegisterResponse.from(group);
    }

    @Transactional
    public ChildCareGroupModifiedResponse modifiedChildCareGroup(final Long groupId,
                                                                 final Member member,
                                                                 final ChildCareGroupModifiedRequest modifiedRequest) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        ContentPermissionVerifier.verifyModifyPermission(childCareGroup.getWriter(), member);

        modifyGroup(childCareGroup, modifiedRequest);

        return ChildCareGroupModifiedResponse.of(groupId);
    }

    @Transactional(readOnly = true)
    public ChildCareGroupLookupResponse lookupChildCareGroup(final Long groupId,
                                                             final Member member) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        return ChildCareGroupLookupResponse.of(childCareGroup);
    }

    @Transactional
    public ChildCareGroupDeleteResponse removeChildCareGroup(final Long groupId, final Member member) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        ContentPermissionVerifier.verifyModifyPermission(childCareGroup.getWriter(), member);

        deleteGroup(childCareGroup, member);

        return ChildCareGroupDeleteResponse.of(groupId);
    }

    @Transactional
    public void withdrawGroup(final Long groupId, final Member member) {
        ChildCareGroup group = generateChildCareGroupById(groupId);
        group.isWriter(member);
        group.validateMemberIsParticipating(member);

        GroupParticipatingMember groupParticipatingMember = group.withdrawMember(member);
        groupParticipatingMemberRepository.delete(groupParticipatingMember);
    }

    @Transactional
    public void applyChildCareGroup(final Long groupId, final Member member,
                                    final ChildCareGroupApplyRequest applyRequest) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);
        childCareGroup.isWriter(member);

        Child child = childService.checkChildId(member, Optional.ofNullable(applyRequest.getChildId()));

        GroupApply groupApply = GroupApply.of(applyRequest, child, member, childCareGroup);
        groupApplyRepository.save(groupApply);

        childCareGroup.addApply(groupApply);
    }

    @Transactional
    public void approveGroupApply(final Long groupId, final Member member,
                                  final Long applyId) {
        ChildCareGroup group = generateChildCareGroupById(groupId);
        group.isNotWriter(member);

        GroupApply groupApply = groupApplyRepository.getById(applyId);
        group.checkApplyIncluding(groupApply);

        approveGroupApply(group, groupApply);
        groupApplyRepository.delete(groupApply);

    }

    @NoAccessCheck
    @Transactional(readOnly = true)
    public ChildCareGroup generateChildCareGroupById(final Long id) {
        ChildCareGroup childCareGroup = childCareGroupRepository.getById(id);
        log.info("Generate child care group id: {}", id);

        return childCareGroup;
    }

    private void validateGroupName(final String name) {
        if (childCareGroupRepository.existsByName(name)) {
            throw new AlreadyExistsGroupNameException();
        }
    }

    private void modifyGroup(final ChildCareGroup childCareGroup,
                             final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        childCareGroup.modifiedFor(childCareGroupModifiedRequest);
        changeProfileImage(childCareGroupModifiedRequest.getProfileImageData(), childCareGroup);
        childCareGroupRepository.save(childCareGroup);

        log.info("Update child care group entity id: {}", childCareGroup.getId());
    }

    private void changeProfileImage(final String profileImageData, final ChildCareGroup group) {
        if (StringUtils.hasText(profileImageData)) {
            Optional<UploadFile> optionalPrevProfileImage = Optional.ofNullable(group.getProfileImage());
            optionalPrevProfileImage.ifPresent(prevProfileImage -> {
                uploadFileRepository.delete(prevProfileImage);
                uploadFileUtils.removeImage(prevProfileImage);
                log.info("Delete previous profile image id: {}", prevProfileImage.getId());
            });

            UploadFile profileImage = uploadFileUtils.saveFileAndConvertImage(profileImageData);
            uploadFileRepository.save(profileImage);

            group.setProfileImage(profileImage);
        }
    }

    private void approveGroupApply(final ChildCareGroup group, final GroupApply apply) {
        GroupParticipatingMember groupParticipatingMember = GroupParticipatingMember
                .of(apply, ParticipationType.PARTICIPATION);
        groupParticipatingMemberRepository.save(groupParticipatingMember);

        group.addParticipatingMember(groupParticipatingMember);
    }

    private void deleteGroup(final ChildCareGroup group, final Member member) {
        group.resetAssociated();
        groupParticipatingMemberRepository.deleteAll(group.getParticipatingMembers());

        Set<GroupApply> applies = group.getApplies();
        groupApplyRepository.deleteAll(applies);

        group.getBoards().forEach(groupBoardService::removeBoard);

        childCareGroupRepository.delete(group);
        log.info("Delete child care group id: {}", group.getId());
    }

}
