package io.wisoft.poomi.service.child_care.group;

import io.wisoft.poomi.domain.child_care.group.apply.GroupApply;
import io.wisoft.poomi.domain.child_care.group.apply.GroupApplyRepository;
import io.wisoft.poomi.domain.child_care.group.participating.child.GroupParticipatingChildRepository;
import io.wisoft.poomi.domain.child_care.group.participating.member.GroupParticipatingMember;
import io.wisoft.poomi.domain.child_care.group.participating.member.GroupParticipatingMemberRepository;
import io.wisoft.poomi.domain.child_care.group.participating.member.ParticipatingType;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.aop.child_care.NoAccessCheck;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupApplyRequest;
import io.wisoft.poomi.global.dto.response.child_care.group.*;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.global.exception.exceptions.AlreadyExistsGroupTitleException;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import io.wisoft.poomi.service.file.S3FileHandler;
import io.wisoft.poomi.service.member.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildCareGroupService {

    private final ChildCareGroupRepository childCareGroupRepository;
    private final GroupParticipatingMemberRepository groupParticipatingMemberRepository;
    private final GroupParticipatingChildRepository groupParticipatingChildRepository;
    private final GroupApplyRepository groupApplyRepository;
    private final UploadFileRepository uploadFileRepository;
    private final S3FileHandler s3FileHandler;
    private final UploadFileUtils uploadFileUtils;

    private final ChildService childService;

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
            final ChildCareGroupRegisterRequest childCareGroupRegisterRequest,
            final String domainInfo) {
        validateGroupName(childCareGroupRegisterRequest.getName());


        ChildCareGroup group = ChildCareGroup.of(member, childCareGroupRegisterRequest);
        log.info("Generate child care group title: {}", group.getName());

        UploadFile profileImage = uploadFileUtils.saveFileAndConvertImage(childCareGroupRegisterRequest.getMetaData());
        uploadFileRepository.save(profileImage);

        group.setProfileImage(profileImage);
        childCareGroupRepository.save(group);
        log.info("Save child care group id: {}", group.getId());

        GroupParticipatingMember groupParticipatingMember = GroupParticipatingMember.builder()
                .participatingType(ParticipatingType.MANAGE)
                .member(member)
                .childCareGroup(group)
                .build();
        groupParticipatingMemberRepository.save(groupParticipatingMember);
        log.info("Save participating group with writer id: {}", member.getId());

        member.addParticipatingGroup(groupParticipatingMember);
        group.addParticipatingMember(groupParticipatingMember);

        return ChildCareGroupRegisterResponse.from(group);
    }

    @Transactional
    public ChildCareGroupModifiedResponse modifiedChildCareGroup(final Long groupId,
                                                                 final Member member,
                                                                 final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        ContentPermissionVerifier.verifyModifyPermission(childCareGroup.getWriter(), member);

        modifyGroup(childCareGroup, childCareGroupModifiedRequest);

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

        approveApply(group, groupApply);

    }

    @NoAccessCheck
    public ChildCareGroup generateChildCareGroupById(final Long id) {
        ChildCareGroup childCareGroup = childCareGroupRepository.getById(id);
        log.info("Generate child care group id: {}", id);

        return childCareGroup;
    }

    private void approveApply(final ChildCareGroup group, final GroupApply apply) {
        Optional.ofNullable(apply.getChild()).ifPresent(child -> {

        });
    }

    private void validateGroupName(final String name) {
        if (childCareGroupRepository.existsByName(name)) {
            throw new AlreadyExistsGroupTitleException();
        }
    }

    private void modifyGroup(final ChildCareGroup childCareGroup,
                             final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        childCareGroup.modifiedFor(childCareGroupModifiedRequest);
        childCareGroupRepository.save(childCareGroup);
        log.info("Update child care group entity id: {}", childCareGroup.getId());
    }

    private void deleteGroup(final ChildCareGroup childCareGroup, final Member member) {
        childCareGroup.resetAssociated();
        childCareGroupRepository.delete(childCareGroup);
        log.info("Delete child care group id: {}", childCareGroup.getId());
    }

}
