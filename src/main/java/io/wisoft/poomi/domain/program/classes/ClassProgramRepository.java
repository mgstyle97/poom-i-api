package io.wisoft.poomi.domain.program.classes;

import io.wisoft.poomi.domain.member.address.AddressTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassProgramRepository extends JpaRepository<ClassProgram, Long> {

    List<ClassProgram> findByAddressTag(final AddressTag addressTag);

}
