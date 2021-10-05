package io.wisoft.poomi.service.child_care.group;

import io.wisoft.poomi.global.aop.childminder.NoAccessCheck;
import io.wisoft.poomi.global.dto.response.child_care.group.*;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupModifiedRequest;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.global.utils.FileUtils;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.domain.child_care.group.image.Image;
import io.wisoft.poomi.domain.child_care.group.image.ImageRepository;
import io.wisoft.poomi.service.child_care.comment.CommentService;
import io.wisoft.poomi.service.child_care.ContentPermissionVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildCareGroupService {

    private final ChildCareGroupRepository childCareGroupRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    private final CommentService commentService;

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildCareGroupLookupResponse> findByAddressTag(final AddressTag addressTag) {
        return childCareGroupRepository.findByAddressTag(addressTag).stream()
                .map(ChildCareGroupLookupResponse::new)
                .collect(Collectors.toList());
    }

    @NoAccessCheck
    @Transactional
    public ChildCareGroupRegisterResponse registerChildCareGroup(final Member member,
                                                                 final ChildCareGroupRegisterRequest childCareGroupRegisterRequest,
                                                                 final List<MultipartFile> images,
                                                                 final String domainInfo) {
        ChildCareGroup childCareGroup = ChildCareGroup.of(member, childCareGroupRegisterRequest);
        log.info("Generate child care group title: {}", childCareGroup.getTitle());

        childCareGroupRepository.save(childCareGroup);
        log.info("Save child care group id: {}", childCareGroup.getId());

        saveFiles(childCareGroup, images, domainInfo);

        memberRepository.save(member);

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
    public ChildCareGroupSinglePageResponse lookupChildCareGroup(final Long groupId,
                                                                 final Member member,
                                                                 final String domainInfo) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        return ChildCareGroupSinglePageResponse.of(childCareGroup, domainInfo);
    }

    @Transactional
    public ChildCareGroupDeleteResponse removeChildCareGroup(final Long groupId, final Member member) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        ContentPermissionVerifier.verifyModifyPermission(childCareGroup.getWriter(), member);

        deleteChildCareGroup(childCareGroup, member);

        return ChildCareGroupDeleteResponse.of(groupId);
    }

    @Transactional
    public void applyChildCareGroup(final Long groupId, final Member member) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(groupId);

        childCareGroup.addApplier(member);
        memberRepository.save(member);
    }

    @Transactional
    public void likeChildCareGroup(final Long id, final Member member) {
        ChildCareGroup childCareGroup = generateChildCareGroupById(id);

        childCareGroup.addLikes(member);
        memberRepository.save(member);
    }

    private void saveFiles(final ChildCareGroup childCareGroup, final List<MultipartFile> images,
                           final String domainInfo) {
        Set<Image> imageEntities = FileUtils.saveImageWithGroupId(childCareGroup, images, domainInfo);

        if (!CollectionUtils.isEmpty(imageEntities)) {
            imageRepository.saveAll(imageEntities);
        }

        childCareGroup.setImages(imageEntities);
    }

    private ChildCareGroup generateChildCareGroupById(final Long id) {
        ChildCareGroup childCareGroup = childCareGroupRepository.getById(id);
        log.info("Generate child care group id: {}", id);

        return childCareGroup;
    }

    private void modifyChildCareGroup(final ChildCareGroup childCareGroup,
                                      final ChildCareGroupModifiedRequest childCareGroupModifiedRequest) {
        childCareGroup.modifiedFor(childCareGroupModifiedRequest);
        childCareGroupRepository.save(childCareGroup);
        log.info("Update child care group entity id: {}", childCareGroup.getId());
    }

    private void deleteChildCareGroup(final ChildCareGroup childCareGroup, final Member member) {
        commentService.deleteAll(childCareGroup.getComments(), childCareGroup.getId());
        imageRepository.deleteAll(childCareGroup.getImages());
        childCareGroup.resetAssociated();
        childCareGroupRepository.delete(childCareGroup);
        log.info("Delete child care group id: {}", childCareGroup.getId());
    }

}
