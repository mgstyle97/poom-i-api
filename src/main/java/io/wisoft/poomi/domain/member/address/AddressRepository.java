package io.wisoft.poomi.domain.member.address;

import io.wisoft.poomi.domain.member.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
