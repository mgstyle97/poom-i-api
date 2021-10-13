package io.wisoft.poomi.service.child_care.group;

import io.wisoft.poomi.domain.child_care.group.apply.GroupApply;
import io.wisoft.poomi.domain.child_care.group.apply.GroupApplyRepository;
import io.wisoft.poomi.domain.child_care.group.participating.member.GroupParticipatingMember;
import io.wisoft.poomi.domain.child_care.group.participating.member.GroupParticipatingMemberRepository;
import io.wisoft.poomi.domain.child_care.group.participating.member.ParticipatingType;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.aop.child_care.NoAccessCheck;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupApplyRequest;
import io.wisoft.poomi.global.dto.response.child_care.group.*;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.global.exception.exceptions.AlreadyExistsGroupTitleException;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.service.child_care.comment.CommentService;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
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
    private final GroupApplyRepository groupApplyRepository;
    private final MemberRepository memberRepository;

    private final CommentService commentService;
    private final ChildService childService;

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildCareGroupLookupResponse> findByAddressTag(final AddressTag addressTag) {
        return childCareGroupRepository.findAllByAddressTag(addressTag).stream()
                .map(ChildCareGroupLookupResponse::new)
                .collect(Collectors.toList());
    }

    @NoAccessCheck
    @Transactional
    public ChildCareGroupRegisterResponse registerChildCareGroup(final Member member,
                                                                 final ChildCareGroupRegisterRequest childCareGroupRegisterRequest) {
        validateGroupTitle(childCareGroupRegisterRequest.getName());

        ChildCareGroup childCareGroup = ChildCareGroup.of(member, childCareGroupRegisterRequest);
        log.info("Generate child care group title: {}", childCareGroup.getName());

        childCareGroupRepository.save(childCareGroup);
        log.info("Save child care group id: {}", childCareGroup.getId());

        GroupParticipatingMember groupParticipatingMember = GroupParticipatingMember.builder()
                .participatingType(ParticipatingType.MANAGE)
                .member(member)
                .childCareGroup(childCareGroup)
                .build();
        groupParticipatingMemberRepository.save(groupParticipatingMember);
        log.info("Save participating group with writer id: {}", member.getId());

        member.addParticipatingGroup(groupParticipatingMember);
        childCareGroup.addParticipatingMember(groupParticipatingMember);

        return ChildCareGroupRegisterResponse.from(childCareGroup);
    }

    @Transactional
    public ChildCareGroupModifiedResponse modifiedChildCareGroup(final Long groupId,
                                                                 final Member member,
                                                                 final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        ContentPermissionVerifier.verifyModifyPermission(childCareGroup.getWriter(), member);

        modifyChildCareGroup(childCareGroup, childCareGroupModifiedRequest);

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

        deleteChildCareGroup(childCareGroup, member);

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

    @NoAccessCheck
    public ChildCareGroup generateChildCareGroupById(final Long id) {
        ChildCareGroup childCareGroup = childCareGroupRepository.getById(id);
        log.info("Generate child care group id: {}", id);

        return childCareGroup;
    }

    private void validateGroupTitle(final String title) {
        if (childCareGroupRepository.existsByName(title)) {
            throw new AlreadyExistsGroupTitleException();
        }
    }

    private void modifyChildCareGroup(final ChildCareGroup childCareGroup,
                                      final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        childCareGroup.modifiedFor(childCareGroupModifiedRequest);
        childCareGroupRepository.save(childCareGroup);
        log.info("Update child care group entity id: {}", childCareGroup.getId());
    }

    private void deleteChildCareGroup(final ChildCareGroup childCareGroup, final Member member) {
        childCareGroup.resetAssociated();
        childCareGroupRepository.delete(childCareGroup);
        log.info("Delete child care group id: {}", childCareGroup.getId());
    }

}
