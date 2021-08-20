package io.wisoft.poomi.service;

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

@Slf4j
@RequiredArgsConstructor
@Service
public class ClassProgramService {

    private final ClassProgramRepository classProgramRepository;

    @Transactional(readOnly = true)
    public List<ClassProgram> findByAddressTag(final AddressTag addressTag) {
        return classProgramRepository.findByAddressTag(addressTag);
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

}
