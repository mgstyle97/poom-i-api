package io.wisoft.poomi.domain.member.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressTagRepository extends JpaRepository<AddressTag, Long> {

    Optional<AddressTag> findByExtraAddress(final String extraAddress);
    boolean existsAddressTagByExtraAddress(final String extraAddress);

    default AddressTag saveAddressTagWithExtraAddress(final String extraAddress) {
        if (this.existsAddressTagByExtraAddress(extraAddress)) {
            return this.findByExtraAddress(extraAddress).get();
        }

        return this.save(new AddressTag(extraAddress));
    }
}
