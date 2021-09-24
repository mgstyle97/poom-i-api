package io.wisoft.poomi.service.childminder.classes;

import io.wisoft.poomi.global.aop.childminder.NoAccessCheck;
import io.wisoft.poomi.global.dto.response.childminder.classes.*;
import io.wisoft.poomi.global.dto.request.childminder.classes.ChildminderClassModifiedRequest;
import io.wisoft.poomi.global.dto.request.childminder.classes.ChildminderClassRegisterRequest;
import io.wisoft.poomi.global.utils.FileUtils;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClassRepository;
import io.wisoft.poomi.domain.childminder.classes.image.Image;
import io.wisoft.poomi.domain.childminder.classes.image.ImageRepository;
import io.wisoft.poomi.service.childminder.comment.CommentService;
import io.wisoft.poomi.service.childminder.ContentPermissionVerifier;
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
public class ChildminderClassService {

    private final ChildminderClassRepository childminderClassRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    private final CommentService commentService;

    @NoAccessCheck
    @Transactional(readOnly = true)
    public List<ChildminderClassLookupResponse> findByAddressTag(final AddressTag addressTag) {
        return childminderClassRepository.findByAddressTag(addressTag).stream()
                .map(ChildminderClassLookupResponse::new)
                .collect(Collectors.toList());
    }

    @NoAccessCheck
    @Transactional
    public ChildminderClassRegisterResponse registerChildminderClass(final Member member,
                                                                     final ChildminderClassRegisterRequest childminderClassRegisterRequest,
                                                                     final List<MultipartFile> images,
                                                                     final String domainInfo) {
        ChildminderClass childminderClass = ChildminderClass.of(member, childminderClassRegisterRequest);
        log.info("Generate childminder class title: {}", childminderClass.getTitle());

        childminderClassRepository.save(childminderClass);
        log.info("Save childminder class id: {}", childminderClass.getId());

        saveFiles(childminderClass, images, domainInfo);

        memberRepository.save(member);

        return ChildminderClassRegisterResponse.from(childminderClass);
    }

    @Transactional
    public ChildminderClassModifiedResponse modifiedChildminderClass(final Long classId,
                                                                     final Member member,
                                                                     final ChildminderClassModifiedRequest childminderClassModifiedRequest) {
        ChildminderClass childminderClass = generateChildminderClassById(classId);

        ContentPermissionVerifier.verifyModifyPermission(childminderClass.getWriter(), member);

        modifyChildminderClass(childminderClass, childminderClassModifiedRequest);

        return ChildminderClassModifiedResponse.of(classId);
    }

    @Transactional(readOnly = true)
    public ChildminderClassSinglePageResponse lookupChildminderClass(final Long classId,
                                                                     final Member member,
                                                                     final String domainInfo) {
        ChildminderClass childminderClass = generateChildminderClassById(classId);

        return ChildminderClassSinglePageResponse.of(childminderClass, domainInfo);
    }

    @Transactional
    public ChildminderClassDeleteResponse removeChildminderClass(final Long classId, final Member member) {
        ChildminderClass childminderClass = generateChildminderClassById(classId);

        ContentPermissionVerifier.verifyModifyPermission(childminderClass.getWriter(), member);

        deleteChildminderClass(childminderClass, member);

        return ChildminderClassDeleteResponse.of(classId);
    }

    @Transactional
    public void applyChildminderClass(final Long classId, final Member member) {
        ChildminderClass childminderClass = generateChildminderClassById(classId);

        childminderClass.addApplier(member);
        memberRepository.save(member);
    }

    @Transactional
    public void likeChildminderClass(final Long id, final Member member) {
        ChildminderClass childminderClass = generateChildminderClassById(id);

        childminderClass.addLikes(member);
        memberRepository.save(member);
    }

    private void saveFiles(final ChildminderClass childminderClass, final List<MultipartFile> images,
                           final String domainInfo) {
        Set<Image> imageEntities = FileUtils.saveImageWithClassId(childminderClass, images, domainInfo);

        if (!CollectionUtils.isEmpty(imageEntities)) {
            imageRepository.saveAll(imageEntities);
        }

        childminderClass.setImages(imageEntities);
    }

    private ChildminderClass generateChildminderClassById(final Long id) {
        ChildminderClass childminderClass = childminderClassRepository.getById(id);
        log.info("Generate childminder class id: {}", id);

        return childminderClass;
    }

    private void modifyChildminderClass(final ChildminderClass childminderClass,
                                        final ChildminderClassModifiedRequest childminderClassModifiedRequest) {
        childminderClass.modifiedFor(childminderClassModifiedRequest);
        childminderClassRepository.save(childminderClass);
        log.info("Update childminder class entity id: {}", childminderClass.getId());
    }

    private void deleteChildminderClass(final ChildminderClass childminderClass, final Member member) {
        commentService.deleteAll(childminderClass.getComments(), childminderClass.getId());
        imageRepository.deleteAll(childminderClass.getImages());
        childminderClass.resetAssociated();
        childminderClassRepository.delete(childminderClass);
        log.info("Delete childminder class id: {}", childminderClass.getId());
    }

}
