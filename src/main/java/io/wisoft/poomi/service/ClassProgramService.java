package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.ClassProgramLookupDto;
import io.wisoft.poomi.bind.dto.ClassProgramRegisterDto;
import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.domain.program.classes.ClassProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClassProgramService {

    private final ClassProgramRepository classProgramRepository;

    @Transactional(readOnly = true)
    public List<ClassProgramLookupDto> findByAddressTag(final AddressTag addressTag) {
        return classProgramRepository.findByAddressTag(addressTag).stream()
                .map(ClassProgramLookupDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClassProgramRegisterDto registerClassProgram(final Member member,
                                                        final ClassProgramRegisterRequest classProgramRegisterRequest) {
        ClassProgram classProgram = ClassProgram.of(member, classProgramRegisterRequest);
        log.info("Generate class program title: {}", classProgram.getTitle());

        classProgramRepository.save(classProgram);
        log.info("Save class program id: {}", classProgram.getId());

        return ClassProgramRegisterDto.from(classProgram);
    }

    @Transactional
    public void applyClassProgram(final Long id, final Member member) {
        ClassProgram classProgram = generateClassProgramById(id);

        classProgram.addApplier(member);
    }

    @Transactional
    public void likeClassProgram(final Long id, final Member member) {
        ClassProgram classProgram = generateClassProgramById(id);

        classProgram.addLikes(member);
    }

    private ClassProgram generateClassProgramById(final Long id) {
        ClassProgram classProgram = classProgramRepository.findClassProgramById(id);
        log.info("Generate class program id: {}", id);

        return classProgram;
    }

}
