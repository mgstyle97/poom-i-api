package io.wisoft.poomi.repository;

import io.wisoft.poomi.domain.member.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
