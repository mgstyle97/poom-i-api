package io.wisoft.poomi.repository;

import io.wisoft.poomi.domain.member.address.AddressTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressTagRepository extends JpaRepository<AddressTag, Long> {

    Optional<AddressTag> findByName(String name);

}
