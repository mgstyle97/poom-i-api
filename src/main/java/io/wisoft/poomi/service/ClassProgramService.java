package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.*;
import io.wisoft.poomi.bind.request.ClassProgramModifiedRequest;
import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.bind.utils.FileUtils;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.domain.program.classes.ClassProgramRepository;
import io.wisoft.poomi.domain.program.classes.comment.CommentRepository;
import io.wisoft.poomi.domain.program.classes.image.Image;
import io.wisoft.poomi.domain.program.classes.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClassProgramService {

    private final ClassProgramRepository classProgramRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    private final CommentService commentService;

    @Transactional(readOnly = true)
    public List<ClassProgramLookupDto> findByAddressTag(final AddressTag addressTag) {
        return classProgramRepository.findByAddressTag(addressTag).stream()
                .map(ClassProgramLookupDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClassProgramRegisterDto registerClassProgram(final Member member,
                                                        final ClassProgramRegisterRequest classProgramRegisterRequest,
                                                        final List<MultipartFile> images) {
        ClassProgram classProgram = ClassProgram.of(member, classProgramRegisterRequest);
        log.info("Generate class program title: {}", classProgram.getTitle());

        classProgramRepository.save(classProgram);
        log.info("Save class program id: {}", classProgram.getId());

        saveFiles(classProgram, images);

        memberRepository.save(member);

        return ClassProgramRegisterDto.from(classProgram);
    }

    @Transactional
    public ClassProgramModifiedDto modifiedClassProgram(final Member member, final Long id,
                                       final ClassProgramModifiedRequest classProgramModifiedRequest) {
        ClassProgram classProgram = generateClassProgramById(id);

        verifyPermission(classProgram, member);

        modifyClassProgram(classProgram, classProgramModifiedRequest);

        return ClassProgramModifiedDto.of(id);
    }

    @Transactional(readOnly = true)
    public ClassProgramSinglePageDto callClassProgramSinglePage(final Long classId, final String domainInfo) {
        ClassProgram classProgram = generateClassProgramById(classId);

        return ClassProgramSinglePageDto.of(classProgram, domainInfo);
    }

    @Transactional
    public ClassProgramDeleteDto removeClassProgram(final Member member, final Long id) {
        ClassProgram classProgram = generateClassProgramById(id);

        verifyPermission(classProgram, member);

        deleteClassProgram(classProgram, member);

        return ClassProgramDeleteDto.of(id);
    }

    @Transactional
    public void applyClassProgram(final Long id, final Member member) {
        ClassProgram classProgram = generateClassProgramById(id);

        classProgram.addApplier(member);
        memberRepository.save(member);
    }

    @Transactional
    public void likeClassProgram(final Long id, final Member member) {
        ClassProgram classProgram = generateClassProgramById(id);

        classProgram.addLikes(member);
        memberRepository.save(member);
    }

    private void saveFiles(final ClassProgram classProgram, final List<MultipartFile> images) {
        Set<Image> imageEntities = FileUtils.saveImageWithClassId(classProgram, images);

        if (!CollectionUtils.isEmpty(imageEntities)) {
            imageRepository.saveAll(imageEntities);
        }

        classProgram.setImages(imageEntities);
    }

    private ClassProgram generateClassProgramById(final Long id) {
        ClassProgram classProgram = classProgramRepository.findClassProgramById(id);
        log.info("Generate class program id: {}", id);

        return classProgram;
    }

    private void verifyPermission(final ClassProgram classProgram, final Member member) {
        classProgram.verifyPermission(member);
        log.info("Verify permission of class program id: {}", classProgram.getId());
    }

    private void modifyClassProgram(final ClassProgram classProgram,
                                    final ClassProgramModifiedRequest classProgramModifiedRequest) {
        classProgram.modifiedFor(classProgramModifiedRequest);
        classProgramRepository.save(classProgram);
        log.info("Update class program entity id: {}", classProgram.getId());
    }

    private void deleteClassProgram(final ClassProgram classProgram, final Member member) {
        commentService.deleteAll(classProgram.getComments(), classProgram.getId());
        imageRepository.deleteAll(classProgram.getImages());
        classProgram.resetAssociated();
        classProgramRepository.delete(classProgram);
        log.info("Delete class program id: {}", classProgram.getId());
    }

}
