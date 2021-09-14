package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.childminder.classes.*;
import io.wisoft.poomi.bind.request.childminder.classes.ChildminderClassModifiedRequest;
import io.wisoft.poomi.bind.request.childminder.classes.ChildminderClassRegisterRequest;
import io.wisoft.poomi.bind.utils.FileUtils;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClassRepository;
import io.wisoft.poomi.domain.childminder.classes.image.Image;
import io.wisoft.poomi.domain.childminder.classes.image.ImageRepository;
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

    @Transactional(readOnly = true)
    public List<ChildminderClassLookupDto> findByAddressTag(final AddressTag addressTag) {
        return childminderClassRepository.findByAddressTag(addressTag).stream()
                .map(ChildminderClassLookupDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChildminderClassRegisterDto registerChildminderClass(final Member member,
                                                                final ChildminderClassRegisterRequest childminderClassRegisterRequest,
                                                                final List<MultipartFile> images,
                                                                final String domainInfo) {
        ChildminderClass childminderClass = ChildminderClass.of(member, childminderClassRegisterRequest);
        log.info("Generate childminder class title: {}", childminderClass.getTitle());

        childminderClassRepository.save(childminderClass);
        log.info("Save childminder class id: {}", childminderClass.getId());

        saveFiles(childminderClass, images, domainInfo);

        memberRepository.save(member);

        return ChildminderClassRegisterDto.from(childminderClass);
    }

    @Transactional
    public ChildminderClassModifiedDto modifiedChildminderClass(final Member member, final Long id,
                                                                final ChildminderClassModifiedRequest childminderClassModifiedRequest) {
        ChildminderClass childminderClass = generateChildminderClassById(id);

        verifyPermission(childminderClass, member);

        modifyChildminderClass(childminderClass, childminderClassModifiedRequest);

        return ChildminderClassModifiedDto.of(id);
    }

    @Transactional(readOnly = true)
    public ChildminderClassSinglePageDto callChildminderClassSinglePage(final Long classId, final String domainInfo) {
        ChildminderClass childminderClass = generateChildminderClassById(classId);

        return ChildminderClassSinglePageDto.of(childminderClass, domainInfo);
    }

    @Transactional
    public ChildminderClassDeleteDto removeChildminderClass(final Member member, final Long id) {
        ChildminderClass childminderClass = generateChildminderClassById(id);

        verifyPermission(childminderClass, member);

        deleteChildminderClass(childminderClass, member);

        return ChildminderClassDeleteDto.of(id);
    }

    @Transactional
    public void applyChildminderClass(final Long id, final Member member) {
        ChildminderClass childminderClass = generateChildminderClassById(id);

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
        ChildminderClass childminderClass = childminderClassRepository.findChildminderClassById(id);
        log.info("Generate childminder class id: {}", id);

        return childminderClass;
    }

    private void verifyPermission(final ChildminderClass childminderClass, final Member member) {
        childminderClass.verifyPermission(member);
        log.info("Verify permission of childminder class id: {}", childminderClass.getId());
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
