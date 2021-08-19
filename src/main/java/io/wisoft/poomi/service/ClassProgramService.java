package io.wisoft.poomi.service;

import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.program.classes.ClassProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClassProgramService {

    private final ClassProgramRepository classProgramRepository;

    @Transactional(readOnly = true)
    public Object findByAddressTag(final AddressTag addressTag) {
        return classProgramRepository.findByAddressTag(addressTag);
    }

}
